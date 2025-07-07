import React, { useRef, useState, useEffect } from "react";
import { ProTable } from "@ant-design/pro-components";
import { Pagination, Button, message } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import axios from "../../api/axiosBackend"; // Corrected Path
import InventoryAdjustmentModal from "../../backcomponents/erp/InventoryAdjustmentModal"; // Corrected Path

const PAGE_SIZE = 10;

function Inventory() {
  const actionRef = useRef();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedProductInfo, setSelectedProductInfo] = useState(null);
  const [searchFilters, setSearchFilters] = useState({});
  const [loading, setLoading] = useState(true);

  const fetchData = async (page = 1, filters = {}) => {
    setLoading(true);
    try {
      const res = await axios.get("/inventory", {
        params: {
          ...filters,
          page: page - 1,
          size: PAGE_SIZE,
          sort: "updatedAt,desc",
        },
      });

      if (res.data && res.data.content) {
        setData(res.data.content);
        setTotal(res.data.totalElements);
      } else {
        setData([]);
        setTotal(0);
      }
    } catch (error) {
      console.error("Inventory data fetching failed:", error);
      message.error("Failed to fetch inventory data.");
      setData([]);
      setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage, searchFilters);
  }, [currentPage, searchFilters]);

  const columns = [
    {
      title: "Product Name",
      dataIndex: "productName",
      key: "productName",
      hideInSearch: true,
    },
    {
      title: "Product Code",
      dataIndex: "productCode",
      key: "productCode",
      copyable: true,
      hideInSearch: true,
    },
    {
      title: "Product ID",
      dataIndex: "productId",
      key: "productId",
      hideInTable: true,
      valueType: 'digit',
      fieldProps: {
        placeholder: "Enter Product ID",
      },
      search: {
        transform: (value) => ({ productId: value }),
      },
    },
    {
      title: "Warehouse Name",
      dataIndex: "warehouseName",
      key: "warehouseName",
      hideInSearch: true,
    },
    {
      title: "Warehouse ID",
      dataIndex: "warehouseId",
      key: "warehouseId",
      hideInTable: true,
      valueType: 'digit',
      fieldProps: {
        placeholder: "Enter Warehouse ID",
      },
      search: {
        transform: (value) => ({ warehouseId: value }),
      },
    },
    {
      title: "Current Stock",
      dataIndex: "currentStock",
      key: "currentStock",
      align: "right",
      hideInSearch: true,
      render: (text) => parseFloat(text).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 }),
    },
    {
      title: "Average Cost",
      dataIndex: "averageCost",
      key: "averageCost",
      align: "right",
      hideInSearch: true,
      render: (text) => `$${parseFloat(text).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`,
    },
    {
      title: "Last Updated",
      dataIndex: "updatedAt",
      key: "updatedAt",
      valueType: "dateTime",
      hideInSearch: true,
      sorter: true,
    },
    {
      title: "Actions",
      key: "action",
      hideInSearch: true,
      render: (_, record) => (
        <Button
          type="link"
          onClick={() => {
            setSelectedProductInfo(record);
            setIsModalOpen(true);
          }}
        >
          Adjust
        </Button>
      ),
    },
  ];

  return (
    <div className="bg-white px-2">
      <ProTable
        columns={columns}
        actionRef={actionRef}
        dataSource={data}
        loading={loading}
        rowKey="inventoryId"
        onSubmit={(params) => {
          const filters = {
            productId: params.productId,
            warehouseId: params.warehouseId,
          };
          setSearchFilters(filters);
          setCurrentPage(1); // Reset to first page on new search
        }}
        onReset={() => {
          setSearchFilters({});
          setCurrentPage(1);
        }}
        pagination={false}
        dateFormatter="string"
        headerTitle="Inventory Management"
        toolBarRender={() => [
          <Button
            key="button"
            icon={<PlusOutlined />}
            onClick={() => {
              setSelectedProductInfo(null); // Reset for new entry
              setIsModalOpen(true);
            }}
            type="primary"
          >
            New Adjustment
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
        />
      </div>

      {isModalOpen && (
        <InventoryAdjustmentModal
          open={isModalOpen}
          onCancel={() => setIsModalOpen(false)}
          onSuccess={() => {
            setIsModalOpen(false);
            fetchData(currentPage, searchFilters);
          }}
          productInfo={selectedProductInfo}
        />
      )}
    </div>
  );
}

export default Inventory;