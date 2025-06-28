import React, { useState, useEffect } from "react";
import { Button, Modal, message } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import axios from "../../api/axiosBackend";
import CRMOpportunityForm from "./CRMOpportunityForm";
import SalesFunnelBoard from "../../backcomponents/crm/SalesFunnelBoard.jsx";

export default function SalesFunnel() {
  const [columns, setColumns] = useState({});
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    const fetchFunnelData = async () => {
      try {
        const res = await axios.get("/opportunities/funnel");

        const funnelData = {};
        res.data.forEach((stageItem) => {
          const key = stageItem.stageDisplayName;
          funnelData[key] = stageItem.opportunities.map((op) => ({
            id: `c${op.opportunityId}`,
            title: op.opportunityName,
            rating: Math.round(op.averageRating ?? 0),
            type: "info",
            ...op,
          }));
        });

        setColumns(funnelData);
      } catch (error) {
        console.error("載入商機漏斗失敗：", error);
        message.error("無法載入商機資料");
      }
    };

    fetchFunnelData();
  }, []);

  const handleCreate = (formValues) => {
    const id = `c${Date.now()}`;
    const stageKey = formValues.stage ?? "INITIAL_CONTACT";

    const newOpportunity = {
      id,
      title: formValues.opportunityName,
      rating: Math.round(formValues.averageRating ?? 1),
      type: "info",
      ...formValues,
    };

    setColumns((prev) => ({
      ...prev,
      [stageKey]: [...(prev[stageKey] || []), newOpportunity],
    }));

    setModalOpen(false);
  };

  return (
    <div className="p-4">
      <div className="flex justify-end items-center mb-4">
        <Button
          type="primary"
          size="large"
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
