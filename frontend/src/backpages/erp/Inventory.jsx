
import React, { useRef, useState, useEffect } from "react";
import { ProTable } from "@ant-design/pro-components";
import { Pagination, InputNumber, Button, message } from "antd"; // Added Button, message
import { PlusOutlined } from "@ant-design/icons"; // For the button icon
import axios from "../../api/axiosBackend";
import InventoryAdjustmentModal from "../../backcomponents/erp/InventoryAdjustmentModal"; // Import the modal

const PAGE_SIZE = 10; // Standard page size

function Inventory() {
  const actionRef = useRef();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  // const [selectedProductInfo, setSelectedProductInfo] = useState(null); // For pre-filling modal if triggered from a row
  const [searchFilters, setSearchFilters] = useState({});
  const [loading, setLoading] = useState(true);

  const fetchData = async (page = 1, filters = {}) => { // Make sure this is being called correctly
    setLoading(true);
    // setData([]); // Let's not clear data immediately to avoid flickering if total is 0 initially
    try {
      const res = await axios.get("/api/inventory", {
        params: {
          ...filters,
          page: page - 1,
          size: PAGE_SIZE,
          sort: "lastUpdatedAt,desc",
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
      setData([]); // Ensure data is empty on error
      setTotal(0); // Ensure total is 0 on error
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
      hideInSearch: true, // Will search by productId for now
    },
    {
      title: "Product Code",
      dataIndex: "productCode",
      key: "productCode",
      copyable: true,
      hideInSearch: true, // Will search by productId for now
    },
    {
      title: "Product ID", // Search field for Product
      dataIndex: "productId",
      key: "productId",
      hideInTable: true, // Don't show this column in table, only for search
      valueType: 'digit',
      fieldProps: { // Props for the search input
        placeholder: "Enter Product ID",
      },
      search: { // Custom transform for search
        transform: (value) => ({ productId: value }),
      },
    },
    {
      title: "Warehouse Name",
      dataIndex: "warehouseName",
      key: "warehouseName",
      hideInSearch: true, // Will search by warehouseId for now
    },
    {
      title: "Warehouse ID", // Search field for Warehouse
      dataIndex: "warehouseId",
      key: "warehouseId",
      hideInTable: true, // Don't show this column in table, only for search
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
      dataIndex: "lastUpdatedAt",
      key: "lastUpdatedAt",
      valueType: "dateTime",
      hideInSearch: true,
      sorter: true, // Allow sorting by this column
    },
    // No 'Actions' column for now, will be added with Adjust Inventory functionality
  ];

  return (
    <div className="bg-white px-2">
      <ProTable
        columns={columns}
        actionRef={actionRef}
        dataSource={data}
        loading={loading}
        rowKey="inventoryId" // Unique key for each row
        search={{
          labelWidth: "auto",
          searchText: "Search",
          resetText: "Clear",
          // collapsed: false, // Optional: Keep search expanded by default
        }}
        pagination={false} // We use a custom pagination component
        dateFormatter="string"
        headerTitle="Inventory Management"
        options={false} // Disable density, full screen, reload, settings for now
        onSubmit={(params) => { // Called when search form is submitted
          // ProTable passes all form values, including undefined ones if not filled.
          // We filter out undefined/null values before setting searchFilters
          const activeFilters = {};
          if (params.productId) activeFilters.productId = params.productId;
          if (params.warehouseId) activeFilters.warehouseId = params.warehouseId;

          setSearchFilters(activeFilters);
          setCurrentPage(1); // Reset to first page on new search
        }}
        // toolBarRender can be added later for "Adjust Inventory" button
        toolBarRender={() => [
          <Button
            key="adjust"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => {
              // setSelectedProductInfo(null); // For a general adjustment
              setIsModalVisible(true);
            }}
          >
            Adjust Inventory
          </Button>,
        ]}
      />

      {total > 0 && (
        <div className="flex justify-center py-4">
          <Pagination
            current={currentPage}
            pageSize={PAGE_SIZE}
            total={total}
            onChange={(page) => setCurrentPage(page)}
            showSizeChanger={false} // Optional: if you want to allow changing page size
          />
        </div>
      )}
      <InventoryAdjustmentModal
        visible={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        onSuccess={() => {
          setIsModalVisible(false);
          fetchData(currentPage, searchFilters); // Refresh data
        }}
        // productInfo={selectedProductInfo} // Pass selected product info if opening modal from a row action
      />
    </div>
  );
}

export default Inventory;