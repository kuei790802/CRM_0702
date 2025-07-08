import React, { useState, useEffect } from "react";
import {
  Card,
  Form,
  Input,
  DatePicker,
  Button,
  Select,
  Table,
  InputNumber,
  message,
  Space,
} from "antd";
import axios from "../../api/axiosBackend";
import { useNavigate } from "react-router-dom";

const { Option } = Select;

const ERPNewPurchaseOrderForm = () => {
  const [form] = Form.useForm();
  const [details, setDetails] = useState([]);
  const [loading, setLoading] = useState(false);
  const [products, setProducts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await axios.get("/products/simple-list");
        // Handle different possible response formats
        const productsData = Array.isArray(res.data)
          ? res.data
          : Array.isArray(res.data?.content)
          ? res.data.content
          : [];
        setProducts(productsData);
      } catch (err) {
        console.error("商品資料載入失敗:", err);
        message.error("商品資料載入失敗");
        setProducts([]); // Ensure products is always an array
      }
    };

    void fetchProducts();
  }, []);

  const addDetail = () => {
    setDetails([
      ...details,
      {
        key: Date.now(),
        productId: null,
        quantity: 1,
        unitPrice: 0,
        warehouseId: 1,
      },
    ]);
  };

  const removeDetail = (itemKey) => {
    setDetails(details.filter((item) => item.key !== itemKey));
  };

  const updateDetail = (itemKey, field, value) => {
    setDetails(
      details.map((item) =>
        item.key === itemKey ? { ...item, [field]: value } : item
      )
    );
  };

  const handleSubmit = async (values) => {
    if (details.length === 0) {
      message.warning("請至少新增一項商品明細");
      return;
    }

    // 驗證每個明細項目是否完整
    const invalidDetails = details.filter(
      (item) =>
        !item.productId ||
        !item.quantity ||
        item.unitPrice === null ||
        item.unitPrice === undefined
    );

    if (invalidDetails.length > 0) {
      message.error("請完善所有商品明細的資料");
      return;
    }

    // 確保所有數值都是數字類型且格式正確
    const payload = {
      supplierId: parseInt(values.supplierId),
      orderDate: values.orderDate.format("YYYY-MM-DD"),
      currency: "TWD",
      remarks: values.remarks || "",
      details: details.map(({ key, ...item }) => ({
        productId: parseInt(item.productId),
        quantity: parseFloat(item.quantity).toString(), // 轉為字串傳送
        unitPrice: parseFloat(item.unitPrice).toString(), // 轉為字串傳送
        warehouseId: parseInt(values.warehouseId),
      })),
    };

    console.log("發送的數據:", payload);

    try {
      setLoading(true);
      // 修正 API 路徑：使用 /purchaseOrders 而非 /purchase-orders
      const response = await axios.post("/purchaseOrders", payload);
      console.log("創建成功:", response.data);
      message.success("進貨單新增成功");
      form.resetFields();
      setDetails([]);
      navigate("/erp/purchaseOrders");
    } catch (err) {
      console.error("完整錯誤信息:", err);
      message.error("進貨單新增失敗");

      // 顯示更詳細的錯誤信息
      if (err.response?.data) {
        console.error("後端返回的錯誤:", err.response.data);
        if (err.response.data.message) {
          message.error(`詳細錯誤: ${err.response.data.message}`);
        } else if (typeof err.response.data === "string") {
          message.error(`錯誤信息: ${err.response.data}`);
        }
      }
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      title: "商品名稱",
      dataIndex: "productId",
      render: (val, record) => (
        <Select
          style={{ width: "100%" }}
          placeholder="請選擇商品"
          value={val}
          onChange={(value) => updateDetail(record.key, "productId", value)}
          showSearch
          optionFilterProp="children"
        >
          {products.map((product) => (
            <Option key={product.productId} value={product.productId}>
              {product.productName}
            </Option>
          ))}
        </Select>
      ),
    },
    {
      title: "數量",
      dataIndex: "quantity",
      render: (val, record) => (
        <InputNumber
          min={0.01}
          step={0.01}
          value={val}
          onChange={(value) => updateDetail(record.key, "quantity", value)}
          placeholder="請輸入數量"
        />
      ),
    },
    {
      title: "單價",
      dataIndex: "unitPrice",
      render: (val, record) => (
        <InputNumber
          min={0}
          step={0.01}
          value={val}
          onChange={(value) => updateDetail(record.key, "unitPrice", value)}
          placeholder="請輸入單價"
        />
      ),
    },
    {
      title: "操作",
      render: (text, record) => (
        <Button danger onClick={() => removeDetail(record.key)}>
          刪除
        </Button>
      ),
    },
  ];

  return (
    <Card title="新增進貨單" loading={loading}>
      <Form form={form} layout="vertical" onFinish={handleSubmit}>
        <Form.Item
          label="供應商ID"
          name="supplierId"
          rules={[{ required: true, message: "請輸入供應商ID" }]}
        >
          <InputNumber
            style={{ width: "100%" }}
            min={1}
            placeholder="請輸入供應商ID"
          />
        </Form.Item>

        <Form.Item
          label="倉庫ID"
          name="warehouseId"
          rules={[{ required: true, message: "請輸入倉庫ID" }]}
          initialValue={1}
        >
          <InputNumber
            style={{ width: "100%" }}
            min={1}
            placeholder="請輸入倉庫ID"
          />
        </Form.Item>

        <Form.Item
          label="訂單日期"
          name="orderDate"
          rules={[{ required: true, message: "請選擇訂單日期" }]}
        >
          <DatePicker style={{ width: "100%" }} />
        </Form.Item>

        <Form.Item label="備註" name="remarks">
          <Input.TextArea rows={3} placeholder="請輸入備註（可選）" />
        </Form.Item>

        <Form.Item label="進貨明細">
          <Table
            dataSource={details}
            columns={columns}
            pagination={false}
            rowKey="key"
            locale={{ emptyText: "請點擊下方按鈕新增商品明細" }}
          />
          <Button type="dashed" onClick={addDetail} className="mt-2" block>
            + 新增商品明細
          </Button>
        </Form.Item>

        <Form.Item>
          <Space>
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              disabled={details.length === 0}
            >
              儲存進貨單
            </Button>
            <Button htmlType="reset" onClick={() => setDetails([])}>
              清除表單
            </Button>
            <Button onClick={() => navigate("/erp/purchaseOrders")}>
              返回列表
            </Button>
          </Space>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default ERPNewPurchaseOrderForm;
