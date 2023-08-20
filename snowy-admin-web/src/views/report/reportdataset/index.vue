<template>
    <a-card :bordered="false">
        <s-table
            ref="table"
            :columns="columns"
            :data="loadData"
            :alert="options.alert.show"
            bordered
            :row-key="(record) => record.id"
            :tool-config="toolConfig"
            :row-selection="options.rowSelection"
        >
            <template #operator class="table-operator">
                <a-space>
                    <a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('gaeaReportDataSetAdd')">
                        <template #icon><plus-outlined /></template>
                        新增
                    </a-button>
                    <xn-batch-delete
                        v-if="hasPerm('gaeaReportDataSetBatchDelete')"
                        :selectedRowKeys="selectedRowKeys"
                        @batchDelete="deleteBatchGaeaReportDataSet"
                    />
                </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)" v-if="hasPerm('gaeaReportDataSetEdit')">编辑</a>
                        <a-divider type="vertical" v-if="hasPerm(['gaeaReportDataSetEdit', 'gaeaReportDataSetDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteGaeaReportDataSet(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('gaeaReportDataSetDelete')">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="reportdataset">
    import Form from './form.vue'
    import gaeaReportDataSetApi from '@/api/report/gaeaReportDataSetApi'
    const table = ref()
    const formRef = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    const columns = [
        {
            title: '数据集编码',
            dataIndex: 'setCode'
        },
        {
            title: '数据集名称',
            dataIndex: 'setName'
        },
        {
            title: '数据集描述',
            dataIndex: 'setDesc'
        },
        {
            title: '数据源编码',
            dataIndex: 'sourceCode'
        },
        {
            title: '动态查询sql或者接口中的请求体',
            dataIndex: 'dynSentence'
        },
        {
            title: '结果案例',
            dataIndex: 'caseResult'
        },
        {
            title: '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
            dataIndex: 'enableFlag'
        },
        {
            title: '创建人',
            dataIndex: 'createBy'
        },
        {
            title: '更新人',
            dataIndex: 'updateBy'
        },
        {
            title: 'VERSION',
            dataIndex: 'version'
        },
        {
            title: 'SET_TYPE',
            dataIndex: 'setType'
        },
    ]
    // 操作栏通过权限判断是否显示
    if (hasPerm(['gaeaReportDataSetEdit', 'gaeaReportDataSetDelete'])) {
        columns.push({
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width: '150px'
        })
    }
    const selectedRowKeys = ref([])
    // 列表选择配置
    const options = {
        // columns数字类型字段加入 needTotal: true 可以勾选自动算账
        alert: {
            show: true,
            clear: () => {
                selectedRowKeys.value = ref([])
            }
        },
        rowSelection: {
            onChange: (selectedRowKey, selectedRows) => {
                selectedRowKeys.value = selectedRowKey
            }
        }
    }
    const loadData = (parameter) => {
        return gaeaReportDataSetApi.gaeaReportDataSetPage(parameter).then((data) => {
            return data
        })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const deleteGaeaReportDataSet = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        gaeaReportDataSetApi.gaeaReportDataSetDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchGaeaReportDataSet = (params) => {
        gaeaReportDataSetApi.gaeaReportDataSetDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
</script>
