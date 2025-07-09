import React, { useEffect, useState } from "react";
import {
  Descriptions,
  Card,
  Spin,
  Table,
  Input,
  Button,
  Modal,
  App,
} from "antd";
import { useParams, useNavigate } from "react-router-dom";
import axios from "../../api/axiosBackend";

const ERPOrderForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { message, modal } = App.useApp(); // Use App.useApp() hook
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [remarks, setRemarks] = useState("");
  const [shipping, setShipping] = useState(false);
  const [confirming, setConfirming] = useState(false);
  const [warehouseId, setWarehouseId] = useState(1); // 預設倉庫 ID
  const [showShipModal, setShowShipModal] = useState(false); // 出貨確認 Modal

  useEffect(() => {
    const fetchOrder = async () => {
      setLoading(true);
      try {
        const res = await axios.get(`/sales-orders/${id}`);
        setOrder(res.data);
        setRemarks(res.data.remarks);
      } catch (error) {
        message.error("無法載入訂單資料");
      } finally {
        setLoading(false);
      }
    };

    fetchOrder();
  }, [id]);

  const handleSave = async () => {
    try {
      await axios.put(`/sales-orders/${id}`, { ...order, remarks });
      message.success("訂單更新成功");
      setEditing(false);
    } catch (error) {
      message.error("更新失敗");
    }
  };

  const handleConfirm = async () => {
    setConfirming(true);
    try {
      await axios.post(`/sales-orders/${id}/confirm`);
      message.success("訂單確認成功");
      const res = await axios.get(`/sales-orders/${id}`);
      setOrder(res.data);
    } catch (error) {
      message.error("訂單確認失敗，請稍後再試");
    } finally {
      setConfirming(false);
    }
  };

  const handleShip = async () => {
    setShipping(true);
    try {
      await axios.post(`/inventory/sales-orders/${id}/ship`, {
        warehouseId,
      });
      message.success("出貨成功");
      const res = await axios.get(`/sales-orders/${id}`);
      setOrder(res.data);
    } catch (error) {
      message.error("出貨失敗，請稍後再試");
    } finally {
      setShipping(false);
      setShowShipModal(false);
    }
  };

  const columns = [
    { title: "商品代碼", dataIndex: "productCode" },
    { title: "商品名稱", dataIndex: "productName" },
    { title: "數量", dataIndex: "quantity" },
    { title: "單價", dataIndex: "unitPrice", render: (val) => `$${val}` },
    { title: "小計", dataIndex: "itemAmount", render: (val) => `$${val}` },
  ];

  if (loading) {
    return <Spin size="large" />;
  }

  return (
    <>
      <Card title={`訂單詳情: ${order.orderNumber}`}>
        <Descriptions bordered column={2}>
          <Descriptions.Item label="訂單編號">
            {order.orderNumber}
          </Descriptions.Item>
          <Descriptions.Item label="訂單日期">
            {order.orderDate}
          </Descriptions.Item>
          <Descriptions.Item label="訂單狀態">
            {order.orderStatus}
          </Descriptions.Item>
          <Descriptions.Item label="付款狀態">
            {order.paymentStatus}
          </Descriptions.Item>
          <Descriptions.Item label="付款方式">
            {order.paymentMethod}
          </Descriptions.Item>
          <Descriptions.Item label="客戶名稱">
            {order.customerName}
          </Descriptions.Item>
          <Descriptions.Item label="客戶類型">
            {order.customerType}
          </Descriptions.Item>
          <Descriptions.Item label="出貨地址" span={2}>
            {order.shippingAddress}
          </Descriptions.Item>
          <Descriptions.Item label="備註" span={2}>
            {editing ? (
              <Input.TextArea
                value={remarks}
                onChange={(e) => setRemarks(e.target.value)}
                rows={3}
              />
            ) : (
              <div>{remarks || "-"}</div>
            )}
          </Descriptions.Item>
        </Descriptions>
        <div className="my-4">
          <Table
            dataSource={order.details}
            columns={columns}
            rowKey="productId"
            pagination={false}
          />
        </div>
        <Descriptions bordered size="small" column={1}>
          <Descriptions.Item label="未稅總金額">
            ${order.totalNetAmount}
          </Descriptions.Item>
          <Descriptions.Item label="稅額">
            ${order.totalTaxAmount}
          </Descriptions.Item>
          <Descriptions.Item label="總金額">
            ${order.totalAmount}
          </Descriptions.Item>
        </Descriptions>
        <div className="mt-4 text-right space-x-2">
          {editing ? (
            <>
              <Button onClick={() => setEditing(false)}>取消</Button>
              <Button type="primary" onClick={handleSave}>
                儲存
              </Button>
            </>
          ) : (
            <>
              <Button type="primary" onClick={() => setEditing(true)}>
                編輯備註
              </Button>
              {order.orderStatus === "DRAFT" ||
              order.orderStatus === "PENDING" ? (
                <Button
                  type="primary"
                  loading={confirming}
                  onClick={handleConfirm}
                >
                  確認訂單
                </Button>
              ) : null}
              {order.orderStatus === "CONFIRMED" && (
                <Button type="primary" onClick={() => setShowShipModal(true)}>
                  出貨
                </Button>
              )}
            </>
          )}
        </div>
      </Card>

      <Modal
        visible={showShipModal}
        title="確認出貨"
        onOk={handleShip}
        confirmLoading={shipping}
        onCancel={() => setShowShipModal(false)}
        okText="確認出貨"
        cancelText="取消"
      >
        <p>
          是否確認將訂單 <b>{order?.orderNumber}</b> 出貨？
        </p>
        <p>倉庫 ID: {warehouseId}</p>
      </Modal>
    </>
  );
};

export default ERPOrderForm;
