package cn.ahyc.yjz.dto;
/**
 * Page
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/16
 */

import java.util.List;

/**
 * Created by sanlli on 15/10/16.
 */
public class Page<T> {

		private int total;

		private List<T> rows;


		public Page() {
		}

		public Page(int total, List<T> rows) {
				this.total = total;
				this.rows = rows;
		}

		public int getTotal() {
				return total;
		}

		public void setTotal(int total) {
				this.total = total;
		}

		public List<T> getRows() {
				return rows;
		}

		public void setRows(List<T> rows) {
				this.rows = rows;
		}
}
