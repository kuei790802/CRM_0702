import React from "react";
import { PlusOutlined } from "@ant-design/icons";
import {
  Button,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Select,
  Space,
  Switch,
  Tag,
  Rate,
} from "antd";

const { TextArea } = Input;

const stageOptions = [
  { label: "潛在開發", value: "prospecting" },
  { label: "已提案", value: "proposal" },
  { label: "議價中", value: "negotiating" },
  { label: "成交", value: "won" },
  { label: "失敗", value: "lost" },
];

const tagOptions = ["潛在", "高機率", "重點", "轉介紹"];

const CRMOpportunityForm = ({ onSubmit }) => {
  const [form] = Form.useForm();

  return (
    <Form
      form={form}
      layout="vertical"
      style={{ maxWidth: 600 }}
      onFinish={onSubmit}
      initialValues={{
        isHot: false,
      }}
    >
      <Form.Item
        label="商機名稱"
        name="title"
        rules={[{ required: true, message: "請輸入商機名稱" }]}
      >
        <Input placeholder="請輸入商機標題" />
      </Form.Item>

      <Form.Item
        label="客戶名稱"
        name="customerName"
        rules={[{ required: true, message: "請輸入客戶名稱" }]}
      >
        <Input placeholder="請輸入客戶名稱" />
      </Form.Item>

      <Form.Item
        label="銷售階段"
        name="stage"
        rules={[{ required: true, message: "請選擇銷售階段" }]}
      >
        <Select options={stageOptions} placeholder="請選擇階段" />
      </Form.Item>

      <Form.Item
        label="預估金額"
        name="amount"
        rules={[{ required: true, message: "請輸入金額" }]}
      >
        <InputNumber
          style={{ width: "100%" }}
          min={0}
          step={1000}
          formatter={(value) =>
            `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
          }
          parser={(value) => value.replace(/\$\s?|(,*)/g, "")}
        />
      </Form.Item>

      <Form.Item
        label="預計成交日"
        name="expectedCloseDate"
        rules={[{ required: true, message: "請選擇日期" }]}
      >
        <DatePicker style={{ width: "100%" }} />
      </Form.Item>

      <Form.Item label="標籤" name="tags">
        <Select
          mode="tags"
          placeholder="可自由輸入或選擇"
          options={tagOptions.map((tag) => ({ label: tag, value: tag }))}
        />
      </Form.Item>

      <Form.Item label="備註" name="note">
        <TextArea rows={3} placeholder="可輸入相關說明" />
      </Form.Item>

      <Form.Item label="重點程度" name="isHotLevel">
        <Rate count={3} allowClear />
      </Form.Item>

      <Form.Item>
        <Space>
          <Button type="primary" htmlType="submit" icon={<PlusOutlined />}>
            建立商機
          </Button>
          <Button htmlType="reset">清除</Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default CRMOpportunityForm;
