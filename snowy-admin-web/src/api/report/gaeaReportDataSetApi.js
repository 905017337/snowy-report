import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/api/webapp/report/reportdataset/` + url, ...arg)

/**
 * 数据集管理Api接口管理器
 *
 * @author czh
 * @date  2023/08/20 16:14
 **/
export default {
	// 获取数据集管理分页
	gaeaReportDataSetPage(data) {
		return request('page', data, 'get')
	},
	// 提交数据集管理表单 edit为true时为编辑，默认为新增
	gaeaReportDataSetSubmitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除数据集管理
	gaeaReportDataSetDelete(data) {
		return request('delete', data)
	},
	// 获取数据集管理详情
	gaeaReportDataSetDetail(data) {
		return request('detail', data, 'get')
	}
}
