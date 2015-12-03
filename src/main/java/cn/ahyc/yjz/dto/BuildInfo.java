/**
 * BuildInfo
 *
 * @author: sanlai_lee@qq.com
 * @date: 15/10/26
 */
package cn.ahyc.yjz.dto;

/**
 * Created by sanlli on 15/10/26.
 */
public class BuildInfo {

		private String name;

		private String version;

		private String date;

		public BuildInfo() {
		}

		public BuildInfo(String name, String version, String date) {
				this.name = name;
				this.version = version;
				this.date = date;
		}

		public String getName() {
				return name;
		}

		public void setName(String name) {
				this.name = name;
		}

		public String getVersion() {
				return version;
		}

		public void setVersion(String version) {
				this.version = version;
		}

		public String getDate() {
				return date;
		}

		public void setDate(String date) {
				this.date = date;
		}
}
