import React, { useState, useEffect } from 'react';
import { Modal, Form, InputNumber, Select, message } from 'antd';
import axios from '../../api/axiosBackend';
import useBackUserStore from '../../stores/useBackUserStore';

const { Option } = Select;

const InventoryAdjustmentModal = ({ open, onCancel, onSuccess, productInfo }) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const { backUser } = useBackUserStore();

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      setLoading(true);

      // 构建请求体 - 修正欄位名稱
      const payload = {
        productId: productInfo?.productId,
        warehouseId: productInfo?.warehouseId,
        quantity: values.quantity, // 改為 quantity，而不是 quantityChange
        unitCost: values.unitCost || 0,
        movementType: values.movementType,
        documentType: 'MANUAL_ADJUSTMENT',
        userId: backUser?.userId
      };

      // 添加调试日志
      console.log('Sending payload:', payload);

      const response = await axios.post('/inventory/adjust', payload);
      
      if (response.status === 200) {
        message.success('庫存調整成功！');
        form.resetFields();
        onSuccess?.();
        onCancel?.();
      }
    } catch (error) {
      console.error('庫存調整失敗:', error.response?.data || error);
      message.error(
        error.response?.data?.message || 
        '庫存調整失敗，請確認輸入資料是否正確。'
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (open && productInfo) {
      form.setFieldsValue({
        productId: productInfo.productId,
        warehouseId: productInfo.warehouseId,
      });
    }
  }, [open, productInfo, form]);

  return (
    <Modal
      title="調整庫存"
      open={open}
      onOk={handleOk}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      confirmLoading={loading}
      destroyOnClose={true}  // 修正: destroyOnHidden -> destroyOnClose
      maskClosable={false}
    >
      <Form
        form={form}
        layout="vertical"
        initialValues={{
          quantity: 0,
          unitCost: 0,
          movementType: 'ADJUSTMENT_IN' // 改為更常用的預設值
        }}
      >
        <Form.Item
          name="quantity"
          label="調整數量"
          rules={[
            { required: true, message: '請輸入調整數量！' },
            { type: 'number', message: '請輸入有效數字！' }
          ]}
          help="輸入正數增加庫存，負數減少庫存"
        >
          <InputNumber 
            style={{ width: '100%' }} 
            placeholder="例如：10 或 -5"
            step={0.01}
            precision={2}
          />
        </Form.Item>
        
        <Form.Item
          name="unitCost"
          label="單位成本"
          rules={[
            { required: true, message: '請輸入單位成本！' },
            { type: 'number', min: 0, message: '單位成本必須為正數' }
          ]}
        >
          <InputNumber
            style={{ width: '100%' }}
            placeholder="請輸入單位成本"
            prefix="$"
            step={0.01}
            min={0}
            precision={2}
          />
        </Form.Item>

        <Form.Item
          name="movementType"
          label="異動類型"
          rules={[{ required: true, message: '請選擇異動類型！' }]}
        >
          <Select placeholder="請選擇異動類型">
            <Option value="ADJUSTMENT_IN">調整增加</Option>
            <Option value="ADJUSTMENT_OUT">調整減少</Option>
            <Option value="MISCELLANEOUS_RECEIPT">其他入庫</Option>
            <Option value="MISCELLANEOUS_ISSUE">其他出庫</Option>
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default InventoryAdjustmentModal;