import React, { useRef, useState, useEffect } from "react";
import { PlusOutlined, CheckCircleOutlined } from "@ant-design/icons";
import { ProTable, TableDropdown } from "@ant-design/pro-components";
import { Button, Pagination, Tag, message, Modal, Space } from "antd";
import { useNavigate } from "react-router-dom";
import axios from "../../api/axiosBackend";

const { confirm } = Modal;
const PAGE_SIZE = 10;

const ERPPurchaseOrders = () => {
  const actionRef = useRef();
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [searchFilters, setSearchFilters] = useState({});
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const [modal, contextHolder] = Modal.useModal();

  const fetchData = async (page = 1, filters = {}) => {
    setLoading(true);
    setData([]);
    try {
      // 修正 API 路徑
      const res = await axios.get("/purchaseOrders", {
        params: {
          ...filters,
          page: page - 1,
          size: PAGE_SIZE,
          sort: "orderDate,desc",
        },
      });

      setData(res.data.content);
      setTotal(res.data.totalElements);
    } catch (error) {
      console.error("進貨單資料抓取失敗:", error);
      message.error("進貨單資料載入失敗");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage, searchFilters);
  }, [currentPage, searchFilters]);

  const handleEdit = (record) => {
    navigate(`/erp/purchaseOrders/${record.purchaseOrderId}`);
  };

  // 確認進貨單
  const handleConfirmOrder = (record) => {
    modal.confirm({
      title: "確認進貨單",
      content: `確定要確認進貨單 ${record.orderNumber} 嗎？確認後將無法再修改。`,
      okText: "確認",
      cancelText: "取消",
      onOk: async () => {
        try {
          await axios.post(`/purchaseOrders/${record.purchaseOrderId}/confirm`);
          message.success("進貨單確認成功！");
          // 重新載入資料
          fetchData(currentPage, searchFilters);
        } catch (error) {
          console.error("確認失敗:", error);
          message.error("確認失敗");
        }
      },
    });
  };

  // 處理庫存收貨
  const handleReceiveInventory = (record) => {
    modal.confirm({
      title: "確認收貨",
      content: `確定要將進貨單 ${record.orderNumber} 的商品全部收貨入庫嗎？`,
      okText: "確認收貨",
      cancelText: "取消",
      onOk: async () => {
        try {
          await axios.post(
            `/inventory/purchase-orders/${record.purchaseOrderId}/receive`
          );
          message.success("收貨成功！庫存已更新");
          // 重新載入資料
          fetchData(currentPage, searchFilters);
        } catch (error) {
          console.error("收貨失敗:", error);
          message.error(
            error.response?.data?.message || "收貨失敗，請稍後再試"
          );
        }
      },
    });
  };

  // 刪除進貨單
  const handleDelete = (record) => {
    modal.confirm({
      title: "確認刪除",
      content: `確定要刪除進貨單 ${record.orderNumber} 嗎？此操作無法恢復。`,
      okText: "確認刪除",
      okType: "danger",
      cancelText: "取消",
      onOk: async () => {
        try {
          await axios.delete(`/purchaseOrders/${record.purchaseOrderId}`);
          message.success("刪除成功");
          fetchData(currentPage, searchFilters);
        } catch (error) {
          console.error("刪除失敗:", error);
          message.error(
            error.response?.data?.message || "刪除失敗，請稍後再試"
          );
        }
      },
    });
  };

  const columns = [
    {
      title: "供應商名稱",
      dataIndex: "supplierName",
      formItemProps: {
        label: "供應商名稱",
      },
      fieldProps: {
        placeholder: "請輸入供應商名稱",
      },
      search: {
        transform: (value) => ({ keyword: value }), // 後端使用 keyword 參數搜尋
      },
    },
    {
      title: "進貨單號",
      dataIndex: "orderNumber",
      copyable: true,
      ellipsis: true,
      hideInSearch: true,
    },
    {
      title: "狀態",
      dataIndex: "status",
      valueType: "select",
      valueEnum: {
        DRAFT: { text: "草稿", status: "Default" },
        CONFIRMED: { text: "已確認", status: "Processing" },
        PARTIALLY_RECEIVED: { text: "部分收貨", status: "Warning" },
        RECEIVED: { text: "已收貨", status: "Success" },
        CANCELLED: { text: "已取消", status: "Error" },
      },
      render: (_, record) => (
        <Tag color={getStatusColor(record.status)}>
          {getStatusText(record.status)}
        </Tag>
      ),
      search: {
        transform: (value) => ({ status: value }),
      },
    },
    {
      title: "總金額",
      dataIndex: "totalAmount",
      render: (_, record) => `$${record.totalAmount?.toLocaleString() || 0}`,
      hideInSearch: true,
    },
    {
      title: "進貨日期",
      dataIndex: "orderDate",
      valueType: "date",
      hideInSearch: true,
    },
    {
      title: "操作",
      key: "action",
      render: (_, record) => {
        const actions = [
          <Button key="edit" onClick={() => handleEdit(record)}>
            編輯
          </Button>,
          <Button
            key="confirm"
            type="primary"
            onClick={() => handleConfirmOrder(record)}
            disabled={record.status !== "DRAFT"}
          >
            確認
          </Button>,
          <Button
            key="receive"
            onClick={() => handleReceiveInventory(record)}
            disabled={record.status !== "CONFIRMED"}
          >
            收貨
          </Button>,
          <Button
            key="delete"
            type="primary"
            danger
            onClick={() => handleDelete(record)}
          >
            刪除
          </Button>,
        ];

        return <Space size="middle">{actions}</Space>;
      },
      hideInSearch: true,
    },
  ];

  return (
    <div className="bg-white px-2">
      {contextHolder}
      <ProTable
        columns={columns}
        actionRef={actionRef}
        dataSource={data}
        loading={loading}
        rowKey="purchaseOrderId"
        search={{
          labelWidth: "auto",
          searchText: "搜尋",
          resetText: "清除",
        }}
        pagination={false}
        dateFormatter="string"
        headerTitle="進貨單管理"
        options={false}
        onSubmit={(params) => {
          setSearchFilters(params);
          setCurrentPage(1);
        }}
        onReset={() => {
          setSearchFilters({});
          setCurrentPage(1);
        }}
        toolBarRender={() => [
          <Button
            key="new"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => navigate("/erp/purchaseOrders/new")}
          >
            新增進貨單
          </Button>,
        ]}
      />

      <div className="flex justify-center py-4">
        <Pagination
          current={currentPage}
          pageSize={PAGE_SIZE}
          total={total}
          onChange={(page) => setCurrentPage(page)}
          showSizeChanger={false}
          showQuickJumper={true}
          showTotal={(total, range) =>
            `第 ${range[0]}-${range[1]} 項 / 共 ${total} 項`
          }
        />
      </div>
    </div>
  );
};

export default ERPPurchaseOrders;

// 狀態文字與顏色對應（更新為後端真實狀態）
function getStatusText(status) {
  switch (status) {
    case "DRAFT":
      return "草稿";
    case "CONFIRMED":
      return "已確認";
    case "PARTIALLY_RECEIVED":
      return "部分收貨";
    case "RECEIVED":
      return "已收貨";
    case "CANCELLED":
      return "已取消";
    default:
      return "未知";
  }
}

function getStatusColor(status) {
  switch (status) {
    case "DRAFT":
      return "default";
    case "CONFIRMED":
      return "blue";
    case "PARTIALLY_RECEIVED":
      return "orange";
    case "RECEIVED":
      return "green";
    case "CANCELLED":
      return "red";
    default:
      return "gray";
  }
}
