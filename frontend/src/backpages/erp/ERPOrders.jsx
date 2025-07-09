import React, { useRef } from "react";
import { PlusOutlined } from "@ant-design/icons";
import { ProTable, TableDropdown } from "@ant-design/pro-components"; // ✨ 1. 重新匯入 TableDropdown
import { Button, App } from "antd"; // ✨ 2. 不再需要 Popconfirm 和 Space
import { useNavigate } from "react-router-dom";
import axios from "../../api/axiosBackend";

const ERPOrders = () => {
  const actionRef = useRef();
  const navigate = useNavigate();
  const { message, modal } = App.useApp(); // ✨ 3. 我們會用到 modal 來做二次確認

  const handleDeleteOrder = (orderId) => {
    modal.confirm({
      title: "確定要取消這筆訂單嗎？",
      content: "取消後，訂單狀態將變更為「已取消」。此操作無法復原。",
      okText: "確定取消",
      okType: "danger",
      cancelText: "再想想",
      onOk: async () => {
        try {
          await axios.delete(`/sales-orders/${orderId}`);
          message.success("訂單已成功取消");
          actionRef.current?.reload();
        } catch (error) {
          console.error("取消訂單失敗:", error);
          const errorMsg =
            error.response?.data?.message || "操作失敗，請稍後再試";
          message.error(errorMsg);
        }
      },
    });
  };

  const columns = [
    {
      title: "客戶名稱",
      dataIndex: "customerName",
      // ... (其他欄位定義保持不變)
    },
    {
      title: "訂單編號",
      dataIndex: "orderNumber",
      copyable: true,
      ellipsis: true,
      hideInSearch: true,
    },
    {
      title: "狀態",
      dataIndex: "orderStatus",
      valueType: "select",
      valueEnum: {
        DRAFT: { text: "草稿", status: "Default" },
        PENDING: { text: "待處理", status: "Processing" },
        CONFIRMED: { text: "已確認", status: "Processing" },
        PARTIAL_SHIPPED: { text: "部分出貨", status: "Warning" },
        SHIPPED: { text: "已出貨", status: "Success" },
        COMPLETED: { text: "已完成", status: "Success" },
        CANCELLED: { text: "已取消", status: "Error" },
      },
    },
    {
      title: "總金額",
      dataIndex: "totalAmount",
      render: (_, record) => `$${record.totalAmount.toLocaleString()}`,
      hideInSearch: true,
    },
    {
      title: "建立日期",
      dataIndex: "orderDate",
      valueType: "date",
      hideInSearch: true,
    },
    {
      title: "操作",
      valueType: "option",
      key: "option",
      // ✨ 4. 【核心改動】使用 TableDropdown 將所有操作收合
      render: (text, record, _, action) => [
        <TableDropdown
          key="actions"
          onMenuClick={({ key }) => {
            if (key === 'edit') {
              navigate(`/erp/sales/orders/${record.salesOrderId}`);
            } else if (key === 'delete') {
              handleDeleteOrder(record.salesOrderId);
            }
          }}
          menus={[
            { key: 'edit', name: '編輯訂單' },
            { key: 'delete', name: '取消訂單', danger: true }, // danger: true 會讓文字變紅色
          ]}
        />,
      ],
      hideInSearch: true,
    },
  ];

  return (
    <div className="bg-white px-2">
      <ProTable
        columns={columns}
        actionRef={actionRef}
        rowKey="salesOrderId"
        search={{
          labelWidth: "auto",
        }}
        request={async (params = {}) => {
          try {
            const res = await axios.get("/sales-orders", {
              params: {
                page: params.current ? params.current - 1 : 0,
                size: params.pageSize,
                keyword: params.customerName,
                status: params.orderStatus,
                sort: "createdAt,desc",
              },
            });
            return {
              data: res.data.content,
              success: true,
              total: res.data.totalElements,
            };
          } catch (error) {
            console.error("ProTable 訂單資料抓取失敗:", error);
            message.error("訂單資料抓取失敗！");
            return {
              data: [],
              success: false,
              total: 0,
            };
          }
        }}
        pagination={{
          pageSize: 10,
        }}
        dateFormatter="string"
        headerTitle="訂單管理"
        toolBarRender={() => [
          <Button
            key="new"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => navigate("/erp/sales/orders/new")}
          >
            新增訂單
          </Button>,
        ]}
      />
    </div>
  );
};

export default ERPOrders;
