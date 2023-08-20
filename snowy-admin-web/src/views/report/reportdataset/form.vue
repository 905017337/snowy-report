<template>
    <xn-form-container
        :title="formData.id ? '编辑数据集管理' : '增加数据集管理'"
        :width="700"
        :visible="visible"
        :destroy-on-close="true"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="数据集编码：" name="setCode">
                <a-input v-model:value="formData.setCode" placeholder="请输入数据集编码" allow-clear />
            </a-form-item>
            <a-form-item label="数据集名称：" name="setName">
                <a-input v-model:value="formData.setName" placeholder="请输入数据集名称" allow-clear />
            </a-form-item>
            <a-form-item label="数据集描述：" name="setDesc">
                <a-input v-model:value="formData.setDesc" placeholder="请输入数据集描述" allow-clear />
            </a-form-item>
            <a-form-item label="数据源编码：" name="sourceCode">
                <a-input v-model:value="formData.sourceCode" placeholder="请输入数据源编码" allow-clear />
            </a-form-item>
            <a-form-item label="动态查询sql或者接口中的请求体：" name="dynSentence">
                <a-input v-model:value="formData.dynSentence" placeholder="请输入动态查询sql或者接口中的请求体" allow-clear />
            </a-form-item>
            <a-form-item label="结果案例：" name="caseResult">
                <a-input v-model:value="formData.caseResult" placeholder="请输入结果案例" allow-clear />
            </a-form-item>
            <a-form-item label="0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG：" name="enableFlag">
                <a-input v-model:value="formData.enableFlag" placeholder="请输入0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG" allow-clear />
            </a-form-item>
            <a-form-item label="创建人：" name="createBy">
                <a-input v-model:value="formData.createBy" placeholder="请输入创建人" allow-clear />
            </a-form-item>
            <a-form-item label="更新人：" name="updateBy">
                <a-input v-model:value="formData.updateBy" placeholder="请输入更新人" allow-clear />
            </a-form-item>
            <a-form-item label="VERSION：" name="version">
                <a-input v-model:value="formData.version" placeholder="请输入VERSION" allow-clear />
            </a-form-item>
            <a-form-item label="SET_TYPE：" name="setType">
                <a-input v-model:value="formData.setType" placeholder="请输入SET_TYPE" allow-clear />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </xn-form-container>
</template>

<script setup name="gaeaReportDataSetForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import gaeaReportDataSetApi from '@/api/report/gaeaReportDataSetApi'
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
    }
    // 关闭抽屉
    const onClose = () => {
        formRef.value.resetFields()
        formData.value = {}
        visible.value = false
    }
    // 默认要校验的
    const formRules = {
    }
    // 验证并提交数据
    const onSubmit = () => {
        formRef.value.validate().then(() => {
            submitLoading.value = true
            const formDataParam = cloneDeep(formData.value)
            gaeaReportDataSetApi
                .gaeaReportDataSetSubmitForm(formDataParam, formDataParam.id)
                .then(() => {
                    onClose()
                    emit('successful')
                })
                .finally(() => {
                    submitLoading.value = false
                })
        })
    }
    // 抛出函数
    defineExpose({
        onOpen
    })
</script>
