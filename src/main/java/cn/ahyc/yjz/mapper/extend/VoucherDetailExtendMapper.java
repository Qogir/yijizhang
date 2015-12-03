package cn.ahyc.yjz.mapper.extend;

import java.util.List;
import java.util.Map;

import cn.ahyc.yjz.mapper.base.VoucherDetailMapper;
import cn.ahyc.yjz.model.VoucherDetail;

/**
 * @author chengjiarui 1256064203@qq.com
 * @ClassName: VoucherDetailExtendMapper
 * @Description: TODO
 * @date 2015年10月18日 上午10:13:24
 */
public interface VoucherDetailExtendMapper extends VoucherDetailMapper {


    /**
     * 查询损益类记账凭证详情
     *
     * @param username
     * @return
     */
    List<VoucherDetail> selectProfitAndLoss(Map<String, Object> param);

    /**
     * 查询凭证分录合计
     *
     * @param voucherId
     * @return
     */
    Map<String, Object> selectDetailTotal(Map<String, Object> param);

    /**
     * 查询凭证分录列表
     *
     * @param voucherId
     * @param bookId
     * @return
     */
    List<Map<String, Object>> selectDetailList(Map<String, Object> param);


    /**
     * 凭证查询.
     *
     * @param param
     * @return
     */
    List<Map> selectVoucherDetailForSearch(Map param);

    /**
     * 根据关键字凭证查询.
     *
     * @param param
     * @return
     */
    List<Map> selectVoucherDetailForSearchByKeyWord(Map param);

    /**
     * 整理凭证字号.
     *
     * @param periodId
     */
    void resetVoucherNo(Long periodId);

    /**
     * 明细账查询.
     *
     * @param periodId
     */
    List<Map> searchDetail(Map param);

    /**
     * 叶子节点添加叶子节点，该叶子节点关联的凭证改成该叶子节点的子节点。
     *
     * @param param
     */
    void updateToNewSubjectCode(Map param);
}
