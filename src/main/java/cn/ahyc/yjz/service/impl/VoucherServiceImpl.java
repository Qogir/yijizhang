package cn.ahyc.yjz.service.impl;

import cn.ahyc.yjz.mapper.extend.AccountSubjectExtendMapper;
import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.mapper.extend.VoucherDetailExtendMapper;
import cn.ahyc.yjz.mapper.extend.VoucherExtendMapper;
import cn.ahyc.yjz.model.*;
import cn.ahyc.yjz.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengjiarui 1256064203@qq.com
 * @ClassName: VoucherServiceImpl
 * @Description: TODO
 * @date 2015年10月18日 上午10:08:41
 */
@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherExtendMapper voucherExtendMapper;

    @Autowired
    private VoucherDetailExtendMapper voucherDetailExtendMapper;

    @Autowired
    private AccountSubjectExtendMapper accountSubjectExtendMapper;

    @Autowired
    private SubjectBalanceExtendMapper subjectBalanceExtendMapper;

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.VoucherService#save(cn.ahyc.yjz.model.Voucher,
     * java.util.List)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(Voucher voucher, List<VoucherDetail> details) {
        long voucherId;
        int voucherNo;
        /** 新增、更新记账凭证 **/
        if (voucher != null && voucher.getId() != null) {
            voucherId = voucher.getId();
            voucherNo = voucher.getVoucherNo();
            voucherExtendMapper.updateByPrimaryKeySelective(voucher);
            /** 删除凭证明细 **/
            VoucherDetailExample example = new VoucherDetailExample();
            VoucherDetailExample.Criteria criteria = example.createCriteria();
            criteria.andVoucherIdEqualTo(voucherId);
            voucherDetailExtendMapper.deleteByExample(example);
        } else {
            voucherNo = queryNextVoucherNo(voucher.getPeriodId());
            voucher.setVoucherNo(voucherNo);
            voucherExtendMapper.insertSelectiveReturnId(voucher);
            voucherId = voucher.getId();
        }
        /** 新增凭证明细 **/
        for (VoucherDetail detail : details) {
            detail.setVoucherId(voucherId);
            detail.setId(null);
            voucherDetailExtendMapper.insertSelective(detail);
        }
        /** 科目余额统计 **/
        subjectBalanceExtendMapper.insertOrUpdateSubjectBalance(voucher.getPeriodId());
        subjectBalanceExtendMapper.collectSubjectBalance(voucher.getPeriodId());
        return voucher.getVoucherWord() + "字第" + voucherNo + "号";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherService#queryNextVoucherNo(java.lang.Long)
     */
    @Override
    public int queryNextVoucherNo(Long periodId) {
        return voucherExtendMapper.selectMaxVoucherNo(periodId) + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherService#queryVoucherDetailList(java.lang.Long,
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public List<Map<String, Object>> queryVoucherDetailList(Long voucherId, Long bookId, Long isreversal) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("voucherId", voucherId);
        param.put("bookId", bookId);
        param.put("isreversal", isreversal);
        return voucherDetailExtendMapper.selectDetailList(param);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.VoucherService#queryVoucher(java.lang.Long)
     */
    @Override
    public Voucher queryVoucher(Long voucherId) {
        return voucherExtendMapper.selectByPrimaryKey(voucherId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherService#queryAccountSubjectList(java.lang.
     * Long)
     */
    @Override
    public List<AccountSubject> queryAccountSubjectList(Long bookId) {
        return accountSubjectExtendMapper.selectLastChildSubject(null, bookId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.VoucherService#queryDetailTotal(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public Map<String, Object> queryDetailTotal(Long voucherId, Long isreversal) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("voucherId", voucherId);
        param.put("isreversal", isreversal);
        return voucherDetailExtendMapper.selectDetailTotal(param);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.VoucherService#checkNo(java.lang.Integer,
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public int checkNo(Integer no, Long periodId, Long id) {
        VoucherExample example = new VoucherExample();
        cn.ahyc.yjz.model.VoucherExample.Criteria criteria = example.createCriteria();
        criteria.andVoucherNoEqualTo(no);
        criteria.andPeriodIdEqualTo(periodId);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        return voucherExtendMapper.countByExample(example) < 1 ? -1 : queryNextVoucherNo(periodId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.VoucherService#delete(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long voucherId, Long periodId) {
        VoucherDetailExample example = new VoucherDetailExample();
        VoucherDetailExample.Criteria criteria = example.createCriteria();
        criteria.andVoucherIdEqualTo(voucherId);
        voucherDetailExtendMapper.deleteByExample(example);
        voucherExtendMapper.deleteByPrimaryKey(voucherId);
        /** 科目余额统计 **/
        subjectBalanceExtendMapper.insertOrUpdateSubjectBalance(periodId);
        subjectBalanceExtendMapper.collectSubjectBalance(periodId);
    }

    /**
     * 查询最新的7条凭证.
     *
     * @param map
     * @return
     */
    @Override
    public List<Map> latestVouchers(Map map) {
        return voucherExtendMapper.latestVouchers(map);
    }

    @Override
    public boolean checkSubjectCode(String subjectCode, Long id) {
        List<AccountSubject> list = accountSubjectExtendMapper.selectLastChildSubject(subjectCode, id);
        return list != null & list.size() > 0;
    }
}
