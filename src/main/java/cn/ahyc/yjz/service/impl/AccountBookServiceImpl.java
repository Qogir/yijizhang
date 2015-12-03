
package cn.ahyc.yjz.service.impl;/**
 * AccountBookServiceImpl
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/23
 */

import cn.ahyc.yjz.mapper.base.DictValueMapper;
import cn.ahyc.yjz.mapper.base.PeriodMapper;
import cn.ahyc.yjz.mapper.base.SubjectLengthMapper;
import cn.ahyc.yjz.mapper.extend.AccountBookExtendMapper;
import cn.ahyc.yjz.mapper.extend.AccountSubjectTemplateExtendMapper;
import cn.ahyc.yjz.model.*;
import cn.ahyc.yjz.model.DictValueExample.Criteria;
import cn.ahyc.yjz.service.AccountBookService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by sanlli on 15/9/23.
 */
@Service
public class AccountBookServiceImpl implements AccountBookService {

		@Autowired
		private AccountBookExtendMapper accountBookMapper;
		@Autowired
		private DictValueMapper dictValueMapper;
		@Autowired
		private SubjectLengthMapper subjectLengthMapper;
		@Autowired
		private AccountSubjectTemplateExtendMapper accountSubjectTemplateExtendMapper;
		@Autowired
		private PeriodMapper periodMapper;

		/**
		 * 创建账套
		 *
		 * @param accountBook
		 * @throws Exception
		 */
		@Transactional(rollbackFor = Exception.class)
		@Override
		public Long createAccountBook(AccountBook accountBook, int... level) {
				Date startTime = generateStartTime(accountBook);
				//保存账套表
				accountBook.setStartTime(startTime);
				accountBook.setOverFlag(0);
				accountBookMapper.insertSelectiveReturnId(accountBook);
				//保存科目代码长度表
				for (int i = 0; i < level.length; i++) {
						SubjectLength subjectLength = new SubjectLength();
						subjectLength.setBookId(accountBook.getId());
						subjectLength.setLength(level[i]);
						subjectLength.setLevel(i + 1);
						subjectLengthMapper.insertSelective(subjectLength);
				}
				//复制科目体系对应的会计科目数据
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("dictValueId", accountBook.getDictValueId());
				map.put("bookId", accountBook.getId());
				Long resultId= 0L;
				accountSubjectTemplateExtendMapper.copyAccountSubject(map);
				//插入期表，当前期
				Period period = new Period();
				period.setStartTime(startTime);
				period.setCurrentPeriod(accountBook.getInitPeriod());
				period.setFlag(1);
				period.setBookId(accountBook.getId());
				period.setEndFlag(0);
				int result = periodMapper.insertSelective(period);
				if(result>0){
					resultId= accountBook.getId();
				}
				return resultId;
		}

		//生成账套启用启用,字符串转化成日期
		public Date generateStartTime(AccountBook accountBook) {
				String str = accountBook.getInitYear() + "-" + accountBook.getInitPeriod() + "-01";
				Date date = null;
				try {
						date = DateUtils.parseDate(str,"yyyy-MM-dd");
				} catch (ParseException e) {
						e.printStackTrace();
				}
				return date;
		}

		/**
		 * 根据ID查询账套.
		 *
		 * @param id
		 * @return
		 */
		@Override
		public AccountBook selectAccountBookById(Long id) {
				return accountBookMapper.selectByPrimaryKey(id);
		}


		/**
		 * 根据账套名称查询账套.
		 *
		 * @param name
		 * @return
		 */
		@Override
		public List<AccountBook> selectAccountBookByName(String name) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name",name);
				List<AccountBook> accountBooks = accountBookMapper.selectByName(map);
				return accountBooks;
		}

		/**
		 * 查询账套列表.
		 *
		 * @return
		 */
		@Override
		public List<AccountBook> selectAllAccountBook() {
				return accountBookMapper.selectAll();
		}

		/**
		 * 查询科目体系数据
		 *
		 * @return
		 */
		@Override
		public List<DictValue> selectSubjectSystem() {
				DictValueExample example = new DictValueExample();
				Criteria criteria = example.createCriteria();
				criteria.andDictTypeIdEqualTo(1L);
				return dictValueMapper.selectByExample(example);
		}

		/**
		 * 查询最新的账套.
		 *
		 * @return
		 */
		@Override
		public AccountBook selectLatestAccountBook() {
				return accountBookMapper.selectLatestAccountBook();
		}
}
