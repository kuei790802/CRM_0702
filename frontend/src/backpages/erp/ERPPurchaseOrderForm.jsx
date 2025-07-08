import React, { useState, useEffect, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Form,
  Card,
  Input,
  DatePicker,
  Select,
  Button,
  Space,
  Table,
  InputNumber,
  Popconfirm,
  message,
  Row,
  Col,
  Modal,
} from "antd";
import {
  MinusCircleOutlined,
  PlusOutlined,
  SaveOutlined,
  ArrowLeftOutlined,
  CheckCircleOutlined,
  InboxOutlined,
} from "@ant-design/icons";
import axios from "../../api/axiosBackend";
import dayjs from "dayjs";

const { Option } = Select;
const { TextArea } = Input;
const { confirm } = Modal;

const ERPPurchaseOrderForm = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [products, setProducts] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [warehouses, setWarehouses] = useState([]);
  const [details, setDetails] = useState([]);
  const [isEditing, setIsEditing] = useState(false);
  const [purchaseOrder, setPurchaseOrder] = useState(null);
  const { id } = useParams();
  const navigate = useNavigate();

  // 使用 useCallback 包裝函數以避免 useEffect 依賴警告
  const fetchSuppliers = useCallback(async () => {
    try {
      const response = await axios.get("/erp/suppliers");
      const supplierData = response.data || [];
      setSuppliers(Array.isArray(supplierData) ? supplierData : []);
    } catch (error) {
      console.error("供應商資料抓取失敗:", error);
      message.error("供應商資料載入失敗");
    }
  }, []);

  const fetchProducts = useCallback(async () => {
    try {
      const response = await axios.get("/products");
      const productData = response.data?.content || response.data || [];
      setProducts(Array.isArray(productData) ? productData : []);
    } catch (error) {
      console.error("產品資料抓取失敗:", error);
      message.error("產品資料載入失敗");
    }
  }, []);

  const fetchWarehouses = useCallback(async () => {
    try {
      const response = await axios.get("/erp/warehouses");
      const warehouseData = response.data || [];
      setWarehouses(Array.isArray(warehouseData) ? warehouseData : []);
    } catch (error) {
      console.error("倉庫資料抓取失敗:", error);
      message.error("倉庫資料載入失敗");
    }
  }, []);

  const fetchPurchaseOrder = useCallback(
    async (orderId) => {
      try {
        const response = await axios.get(`/purchaseOrders/${orderId}`);
        const order = response.data;
        setPurchaseOrder(order);

        form.setFieldsValue({
          supplierId: order.supplierId,
          orderDate: dayjs(order.orderDate),
          currency: order.currency,
          remarks: order.remarks,
        });

        const mappedDetails =
          order.details?.map((detail, index) => ({
            key: index,
            id: detail.id,
            productId: detail.productId,
            quantity: detail.quantity,
            unitPrice: detail.unitPrice,
            warehouseId: detail.warehouseId,
            productName: detail.productName,
            warehouseName: detail.warehouseName,
          })) || [];

        setDetails(mappedDetails);
      } catch (error) {
        console.error("進貨單資料抓取失敗:", error);
        message.error("進貨單資料載入失敗");
      }
    },
    [form]
  );

  useEffect(() => {
    const initializeData = async () => {
      await Promise.all([fetchSuppliers(), fetchProducts(), fetchWarehouses()]);

      if (id && id !== "new") {
        setIsEditing(true);
        await fetchPurchaseOrder(id);
      } else {
        // 新增模式，初始化一個空的明細行
        setDetails([
          {
            key: Date.now(),
            productId: null,
            quantity: 1,
            unitPrice: 0,
            warehouseId: null,
          },
        ]);
      }
    };

    initializeData();
  }, [id, fetchSuppliers, fetchProducts, fetchWarehouses, fetchPurchaseOrder]);

  // 確認進貨單
  const handleConfirmOrder = () => {
    confirm({
      title: "確認進貨單",
      content: "確認後此進貨單將無法再修改，且可以進行收貨操作。確定要確認嗎？",
      okText: "確認",
      cancelText: "取消",
      onOk: async () => {
        try {
          setLoading(true);
          await axios.post(`/purchaseOrders/${id}/confirm`);
          message.success("進貨單確認成功！");
          // 重新載入進貨單資料
          await fetchPurchaseOrder(id);
        } catch (error) {
          console.error("確認進貨單失敗:", error);
          message.error(error.response?.data?.message || "確認進貨單失敗");
        } finally {
          setLoading(false);
        }
      },
    });
  };

  // 收取進貨單
  const handleReceiveOrder = () => {
    confirm({
      title: "收取進貨單",
      content: "確定要將此進貨單的所有商品收貨入庫嗎？",
      okText: "確認收貨",
      cancelText: "取消",
      onOk: async () => {
        try {
          setLoading(true);
          await axios.post(`/inventory/purchase-orders/${id}/receive`);
          message.success("收貨成功！庫存已更新");
          // 重新載入進貨單資料
          await fetchPurchaseOrder(id);
        } catch (error) {
          console.error("收貨失敗:", error);
          message.error(
            error.response?.data?.message || "收貨失敗，請稍後再試"
          );
        } finally {
          setLoading(false);
        }
      },
    });
  };

  const handleSubmit = async (values) => {
    try {
      setLoading(true);

      // 準備提交的資料
      const submitData = {
        supplierId: values.supplierId,
        orderDate: values.orderDate.format("YYYY-MM-DD"),
        currency: values.currency || "TWD",
        remarks: values.remarks,
        details: details.map((detail) => ({
          productId: detail.productId,
          quantity: detail.quantity,
          unitPrice: detail.unitPrice,
          warehouseId: detail.warehouseId,
        })),
      };

      if (isEditing) {
        await axios.put(`/purchaseOrders/${id}`, submitData);
        message.success("進貨單更新成功");
      } else {
        await axios.post("/purchaseOrders", submitData);
        message.success("進貨單建立成功");
      }

      navigate("/purchaseorders");
    } catch (error) {
      console.error("進貨單提交失敗:", error);
      message.error(isEditing ? "進貨單更新失敗" : "進貨單建立失敗");
    } finally {
      setLoading(false);
    }
  };

  const addDetail = () => {
    const newDetail = {
      key: Date.now(),
      productId: null,
      quantity: 1,
      unitPrice: 0,
      warehouseId: null,
    };
    setDetails([...details, newDetail]);
  };

  const removeDetail = (key) => {
    setDetails(details.filter((detail) => detail.key !== key));
  };

  const updateDetail = (key, field, value) => {
    setDetails(
      details.map((detail) =>
        detail.key === key ? { ...detail, [field]: value } : detail
      )
    );
  };

  const calculateTotal = () => {
    return details.reduce((total, detail) => {
      const itemTotal = (detail.quantity || 0) * (detail.unitPrice || 0);
      return total + itemTotal;
    }, 0);
  };

  // 狀態顯示組件
  const renderStatusTag = () => {
    if (!purchaseOrder) return null;

    const statusConfig = {
      DRAFT: { color: "default", text: "草稿" },
      CONFIRMED: { color: "blue", text: "已確認" },
      PARTIALLY_RECEIVED: { color: "orange", text: "部分收貨" },
      RECEIVED: { color: "green", text: "已收貨" },
      CANCELLED: { color: "red", text: "已取消" },
    };

    const config = statusConfig[purchaseOrder.status] || {
      color: "gray",
      text: "未知",
    };

    return (
      <span
        style={{
          padding: "4px 8px",
          backgroundColor:
            config.color === "default"
              ? "#f5f5f5"
              : config.color === "blue"
              ? "#e6f7ff"
              : config.color === "orange"
              ? "#fff7e6"
              : config.color === "green"
              ? "#f6ffed"
              : config.color === "red"
              ? "#fff2f0"
              : "#fafafa",
          color:
            config.color === "default"
              ? "#666"
              : config.color === "blue"
              ? "#1890ff"
              : config.color === "orange"
              ? "#fa8c16"
              : config.color === "green"
              ? "#52c41a"
              : config.color === "red"
              ? "#ff4d4f"
              : "#666",
          borderRadius: "4px",
          fontSize: "12px",
          fontWeight: "bold",
        }}
      >
        {config.text}
      </span>
    );
  };

  const detailColumns = [
    {
      title: "產品",
      dataIndex: "productId",
      render: (value, record) => (
        <Select
          value={value}
          placeholder="請選擇產品"
          style={{ width: "100%" }}
          onChange={(val) => updateDetail(record.key, "productId", val)}
          disabled={
            purchaseOrder?.status === "CONFIRMED" ||
            purchaseOrder?.status === "RECEIVED"
          }
        >
          {products.map((product) => (
            <Option key={product.productId} value={product.productId}>
              {product.productName || product.name}
            </Option>
          ))}
        </Select>
      ),
    },
    {
      title: "數量",
      dataIndex: "quantity",
      render: (value, record) => (
        <InputNumber
          value={value}
          min={1}
          precision={0}
          onChange={(val) => updateDetail(record.key, "quantity", val)}
          disabled={
            purchaseOrder?.status === "CONFIRMED" ||
            purchaseOrder?.status === "RECEIVED"
          }
        />
      ),
    },
    {
      title: "單價",
      dataIndex: "unitPrice",
      render: (value, record) => (
        <InputNumber
          value={value}
          min={0}
          precision={2}
          formatter={(value) =>
            `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
          }
          parser={(value) => value?.replace(/\$\s?|(,*)/g, "")}
          onChange={(val) => updateDetail(record.key, "unitPrice", val)}
          disabled={
            purchaseOrder?.status === "CONFIRMED" ||
            purchaseOrder?.status === "RECEIVED"
          }
        />
      ),
    },
    {
      title: "倉庫",
      dataIndex: "warehouseId",
      render: (value, record) => (
        <Select
          value={value}
          placeholder="請選擇倉庫"
          style={{ width: "100%" }}
          onChange={(val) => updateDetail(record.key, "warehouseId", val)}
          disabled={
            purchaseOrder?.status === "CONFIRMED" ||
            purchaseOrder?.status === "RECEIVED"
          }
        >
          {warehouses.map((warehouse) => (
            <Option key={warehouse.warehouseId} value={warehouse.warehouseId}>
              {warehouse.name}
            </Option>
          ))}
        </Select>
      ),
    },
    {
      title: "小計",
      render: (_, record) => {
        const subtotal = (record.quantity || 0) * (record.unitPrice || 0);
        return `$${subtotal.toLocaleString()}`;
      },
    },
    {
      title: "操作",
      render: (_, record) => (
        <Popconfirm
          title="確定要刪除這個項目嗎？"
          onConfirm={() => removeDetail(record.key)}
          okText="是"
          cancelText="否"
        >
          <Button
            type="text"
            danger
            icon={<MinusCircleOutlined />}
            disabled={
              details.length === 1 ||
              purchaseOrder?.status === "CONFIRMED" ||
              purchaseOrder?.status === "RECEIVED"
            }
          />
        </Popconfirm>
      ),
    },
  ];

  return (
    <div className="p-4">
      <Card
        title={
          <Space>
            <Button
              icon={<ArrowLeftOutlined />}
              onClick={() => navigate("/erp/purchaseorders")}
            >
              返回列表
            </Button>
            <span>{isEditing ? "編輯進貨單" : "新增進貨單"}</span>
            {renderStatusTag()}
          </Space>
        }
        extra={
          isEditing &&
          purchaseOrder && (
            <Space>
              {purchaseOrder.status === "DRAFT" && (
                <Button
                  type="primary"
                  icon={<CheckCircleOutlined />}
                  onClick={handleConfirmOrder}
                  loading={loading}
                >
                  確認進貨單
                </Button>
              )}
              {purchaseOrder.status === "CONFIRMED" && (
                <Button
                  type="primary"
                  icon={<InboxOutlined />}
                  onClick={handleReceiveOrder}
                  loading={loading}
                  style={{ backgroundColor: "#52c41a", borderColor: "#52c41a" }}
                >
                  收貨入庫
                </Button>
              )}
            </Space>
          )
        }
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          initialValues={{
            orderDate: dayjs(),
            currency: "TWD",
          }}
        >
          <Row gutter={16}>
            <Col span={8}>
              <Form.Item
                label="供應商"
                name="supplierId"
                rules={[{ required: true, message: "請選擇供應商" }]}
              >
                <Select
                  placeholder="請選擇供應商"
                  disabled={
                    purchaseOrder?.status === "CONFIRMED" ||
                    purchaseOrder?.status === "RECEIVED"
                  }
                >
                  {suppliers.map((supplier) => (
                    <Option
                      key={supplier.supplierId}
                      value={supplier.supplierId}
                    >
                      {supplier.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item
                label="進貨日期"
                name="orderDate"
                rules={[{ required: true, message: "請選擇進貨日期" }]}
              >
                <DatePicker
                  style={{ width: "100%" }}
                  disabled={
                    purchaseOrder?.status === "CONFIRMED" ||
                    purchaseOrder?.status === "RECEIVED"
                  }
                />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item label="幣別" name="currency">
                <Select
                  disabled={
                    purchaseOrder?.status === "CONFIRMED" ||
                    purchaseOrder?.status === "RECEIVED"
                  }
                >
                  <Option value="TWD">TWD</Option>
                  <Option value="USD">USD</Option>
                  <Option value="CNY">CNY</Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="備註" name="remarks">
            <TextArea
              rows={3}
              placeholder="請輸入備註（選填）"
              disabled={
                purchaseOrder?.status === "CONFIRMED" ||
                purchaseOrder?.status === "RECEIVED"
              }
            />
          </Form.Item>

          <Card
            title="進貨明細"
            extra={
              (!purchaseOrder ||
                (purchaseOrder.status !== "CONFIRMED" &&
                  purchaseOrder.status !== "RECEIVED")) && (
                <Button
                  type="dashed"
                  onClick={addDetail}
                  icon={<PlusOutlined />}
                >
                  新增明細
                </Button>
              )
            }
          >
            <Table
              dataSource={details}
              columns={detailColumns}
              pagination={false}
              rowKey="key"
            />

            <div className="mt-4 text-right">
              <Space size="large">
                <span>
                  總計：<strong>${calculateTotal().toLocaleString()}</strong>
                </span>
              </Space>
            </div>
          </Card>

          {(!purchaseOrder || purchaseOrder.status === "DRAFT") && (
            <Form.Item className="mt-4">
              <Space>
                <Button
                  type="primary"
                  htmlType="submit"
                  loading={loading}
                  icon={<SaveOutlined />}
                >
                  {isEditing ? "更新進貨單" : "建立進貨單"}
                </Button>
                <Button onClick={() => navigate("/erp/purchaseorders")}>
                  取消
                </Button>
              </Space>
            </Form.Item>
          )}
        </Form>
      </Card>
    </div>
  );
};

export default ERPPurchaseOrderForm;
