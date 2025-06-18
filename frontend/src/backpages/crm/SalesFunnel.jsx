import React, { useState } from "react";
import { Button, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import CRMOpportunityForm from "./CRMOpportunityForm";
import SalesFunnelBoard from "../../backcomponents/crm/SalesFunnelBoard.jsx";

const initialData = {
  new: [
    { id: "c1", title: "開發新客戶 A", rating: 2, type: "info" },
    { id: "c2", title: "LINE 詢價潛在客戶", rating: 1, type: "warning" },
  ],
  evaluated: [
    { id: "c3", title: "B 公司需求評估", rating: 3, type: "success" },
  ],
  proposal: [
    { id: "c4", title: "C 公司報價審核中", rating: 2, type: "warning" },
  ],
  closed: [
    { id: "c5", title: "D 客戶成交完成", rating: 3, type: "success" },
  ],
  won: [],
  lost: [],
  prospecting: [],
  negotiating: [],
};

export default function SalesFunnel() {
  const [columns, setColumns] = useState(initialData);
  const [modalOpen, setModalOpen] = useState(false);

  const handleCreate = (formValues) => {
    const id = `c${Date.now()}`;
    const newOpportunity = {
      id,
      ...formValues,
    };

    const stage = formValues.stage || "new";
    setColumns((prev) => ({
      ...prev,
      [stage]: [...(prev[stage] || []), newOpportunity],
    }));

    setModalOpen(false);
  };

  return (
    <div className="p-4">
      <div className="flex justify-between items-center mb-4">
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => setModalOpen(true)}
        >
          新增商機
        </Button>
      </div>

      <SalesFunnelBoard columns={columns} setColumns={setColumns} />

      <Modal
        open={modalOpen}
        onCancel={() => setModalOpen(false)}
        footer={null}
        title="新增商機"
        destroyOnClose
      >
        <CRMOpportunityForm onSubmit={handleCreate} />
      </Modal>
    </div>
  );
}
