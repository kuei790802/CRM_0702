import React, { useState } from 'react';
import { Modal, Form, InputNumber, Select, Input, message } from 'antd';
import axios from '../../api/axiosBackend';

const { Option } = Select;

const InventoryAdjustmentModal = ({ visible, onCancel, onSuccess, productInfo }) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      setLoading(true);

      // TODO(jules): The backend DTO for /api/inventory/adjust is InventoryAdjustmentDTO
      // It expects: productId, warehouseId, quantity, unitCost, movementType,
      // documentType, documentId, documentItemId, userId (operatorId)
      // For now, documentType, documentId, documentItemId, userId can be omitted or defaulted if backend allows

      const payload = {
        productId: values.productId,
        warehouseId: values.warehouseId,
        quantity: values.quantity,
        unitCost: values.unitCost, // This might be optional or derived
        movementType: values.movementType,
        // operatorId: 1, // Assuming a default user ID for now as per backend TODOs
      };

      await axios.post('/api/inventory/adjust', payload);
      message.success('Inventory adjusted successfully!');
      onSuccess(); // Callback to refresh data and close modal
      form.resetFields();
    } catch (error) {
      console.error('Failed to adjust inventory:', error);
      const errorMsg = error.response?.data?.message || 'Failed to adjust inventory. Please check console for details.';
      message.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  // Pre-fill form if productInfo is provided (e.g., for a quick adjustment from a row)
  // For a general adjustment, these would be empty or selected by user.
  // This example assumes a general adjustment modal first.
  // useEffect(() => {
  //   if (productInfo) {
  //     form.setFieldsValue({
  //       productId: productInfo.productId,
  //       warehouseId: productInfo.warehouseId,
  //       // Potentially current unitCost if available
  //     });
  //   } else {
  //     form.resetFields();
  //   }
  // }, [productInfo, form, visible]);


  return (
    <Modal
      title="Adjust Inventory"
      visible={visible}
      onOk={handleOk}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      confirmLoading={loading}
      destroyOnClose // Reset form state when modal is closed
    >
      <Form form={form} layout="vertical" name="inventoryAdjustmentForm">
        <Form.Item
          name="productId"
          label="Product ID"
          rules={[{ required: true, message: 'Please input the Product ID!' }]}
        >
          <InputNumber style={{ width: '100%' }} placeholder="Enter Product ID" />
        </Form.Item>
        <Form.Item
          name="warehouseId"
          label="Warehouse ID"
          rules={[{ required: true, message: 'Please input the Warehouse ID!' }]}
        >
          <InputNumber style={{ width: '100%' }} placeholder="Enter Warehouse ID" />
        </Form.Item>
        <Form.Item
          name="quantity"
          label="Adjustment Quantity"
          rules={[{ required: true, message: 'Please input the quantity!' }]}
          help="Enter a positive value to increase stock, negative to decrease."
        >
          <InputNumber style={{ width: '100%' }} placeholder="e.g., 10 or -5" />
        </Form.Item>
        <Form.Item
          name="unitCost"
          label="Unit Cost"
          rules={[
            { required: true, message: 'Please input the unit cost!' },
            { type: 'number', min: 0, message: 'Unit cost must be a positive number' }
          ]}
        >
          <InputNumber
            style={{ width: '100%' }}
            placeholder="Enter unit cost if applicable"
            addonBefore="$"
          />
        </Form.Item>
        <Form.Item
          name="movementType"
          label="Movement Type"
          rules={[{ required: true, message: 'Please select a movement type!' }]}
        >
          <Select placeholder="Select a movement type">
            {/* These should ideally come from an enum or config on the backend/shared */}
            <Option value="MANUAL_ADJUSTMENT">Manual Adjustment</Option>
            <Option value="STOCKTAKE_GAIN">Stocktake Gain</Option>
            <Option value="STOCKTAKE_LOSS">Stocktake Loss</Option>
            <Option value="DAMAGE">Damaged Goods</Option>
            <Option value="RETURN_RESTOCK">Return Restock</Option>
            {/* Add other relevant movement types */}
          </Select>
        </Form.Item>
        {/* Optional fields for document linking - can be added later if needed */}
        {/* <Form.Item name="documentType" label="Document Type">
          <Input placeholder="e.g., PO, SO, ADJ" />
        </Form.Item>
        <Form.Item name="documentId" label="Document ID">
          <InputNumber style={{ width: '100%' }} placeholder="Associated Document ID" />
        </Form.Item> */}
      </Form>
    </Modal>
  );
};

export default InventoryAdjustmentModal;
