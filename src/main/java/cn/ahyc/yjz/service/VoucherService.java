package cn.ahyc.yjz.service;
import cn.ahyc.yjz.model.AccountSubject;
import cn.ahyc.yjz.model.Voucher;
import cn.ahyc.yjz.model.VoucherDetail;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: VoucherService
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月18日 上午10:12:35
 * 
 */
public interface VoucherService {

    /**
     * 创建凭证及凭证明细
     * 
     * @param voucher
     * @param details
     */
    String save(Voucher voucher, List<VoucherDetail> details);

    /**
     * 获取凭证明细列表
     * 
     * @param voucherId
     * @param bookId
     * @param isreversal
     * @return
     */
    List<Map<String, Object>> queryVoucherDetailList(Long voucherId, Long bookId, Long isreversal);

    /**
     * 查询记账凭证
     * 
     * @param voucherId
     * @return
     */
    Voucher queryVoucher(Long voucherId);

    /**
     * 获取下一个凭证号
     * 
     * @param periodId
     * @return
     */
    int queryNextVoucherNo(Long periodId);

    /**
     * 查询所有会计科目
     * 
     * @param bookId
     * 
     * @return
     */
    List<AccountSubject> queryAccountSubjectList(Long bookId);

    /**
     * 查询凭证分录合计
     * 
     * @param voucherId
     * @param isreversal
     * @return
     */
    Map<String, Object> queryDetailTotal(Long voucherId, Long isreversal);

    /**
     * 检查凭证号
     * 
     * @param no
     * @param periodId
     * @param id
     * @return
     */
    int checkNo(Integer no, Long periodId, Long id);

    /**
     * 删除凭证
     * 
     * @param voucherId
     * @param periodId
     */
    void delete(Long voucherId, Long periodId);

    /**
     * 查询最新的7条凭证.
     * @param map
     * @return
     */
    List<Map> latestVouchers(Map map);

    /**
     * 检查是否是明细科目
     * @param subjectCode
     * @param id
     * @return
     */
    boolean checkSubjectCode(String subjectCode, Long id);
}
