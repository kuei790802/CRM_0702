import React, { useState } from "react";
import {
  DndContext,
  closestCenter,
  PointerSensor,
  useSensor,
  useSensors,
  useDroppable,
  DragOverlay,
} from "@dnd-kit/core";
import {
  arrayMove,
  SortableContext,
  rectSortingStrategy,
  useSortable,
} from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import { FaStar, FaRegStar, FaClock, FaEdit } from "react-icons/fa";

const initialData = {
  new: [
    { id: "c1", title: "123456", rating: 0 },
    { id: "c2", title: "1234", rating: 0 },
  ],
  evaluated: [
    { id: "c3", title: "12345", rating: 0 },
  ],
  proposal: [
    { id: "c4", title: "123", rating: 0 },
  ],
  closed: [],
};

const columnTitles = {
  new: "新潛在客戶",
  evaluated: "已評估",
  proposal: "提案",
  closed: "成交",
};

export default function SalesFunnelBoard() {
  const [columns, setColumns] = useState(initialData);
  const [overColumnId, setOverColumnId] = useState(null);
  const [activeCard, setActiveCard] = useState(null);
  const [activeId, setActiveId] = useState(null);

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 20,
      },
    })
  );

  const handleDragStart = ({ active }) => {
    const id = active.id;
    setActiveId(id);
    for (const col in columns) {
      const item = columns[col].find((i) => i.id === id);
      if (item) {
        setActiveCard(item);
        break;
      }
    }
  };

  const handleDragOver = ({ active, over }) => {
    if (!over) return;
    const overId = over.id;

    let sourceColumn = null;
    for (const key in columns) {
      if (columns[key].some((item) => item.id === active.id)) {
        sourceColumn = key;
        break;
      }
    }

    let targetColumn = null;
    const isOverColumn = Object.keys(columns).includes(overId);
    if (isOverColumn) {
      targetColumn = overId;
    } else {
      for (const key in columns) {
        if (columns[key].some((item) => item.id === overId)) {
          targetColumn = key;
          break;
        }
      }
    }

    if (!sourceColumn || !targetColumn || sourceColumn === targetColumn) return;

    const activeItem = columns[sourceColumn].find((i) => i.id === active.id);
    const newSource = columns[sourceColumn].filter((i) => i.id !== active.id);
    const newTarget = [...columns[targetColumn], activeItem];

    setColumns({
      ...columns,
      [sourceColumn]: newSource,
      [targetColumn]: newTarget,
    });
    setOverColumnId(targetColumn);
  };

  const handleDragEnd = (event) => {
    const { active, over } = event;
    setActiveCard(null);
    setOverColumnId(null);
    setActiveId(null);
    if (!over) return;

    const activeId = active.id;
    const overId = over.id;

    let sourceColumn = null;
    let targetColumn = null;

    for (const key in columns) {
      if (columns[key].some((item) => item.id === activeId)) {
        sourceColumn = key;
      }
    }

    const isOverColumn = Object.keys(columns).includes(overId);

    if (isOverColumn) {
      targetColumn = overId;
    } else {
      for (const key in columns) {
        if (columns[key].some((item) => item.id === overId)) {
          targetColumn = key;
        }
      }
    }

    if (!sourceColumn || !targetColumn) return;

    if (sourceColumn === targetColumn) {
      const oldIndex = columns[sourceColumn].findIndex((i) => i.id === activeId);
      const newIndex = columns[targetColumn].findIndex((i) => i.id === overId);
      if (oldIndex !== newIndex) {
        const newItems = arrayMove(columns[sourceColumn], oldIndex, newIndex);
        setColumns({ ...columns, [sourceColumn]: newItems });
      }
    }
  };

  return (
    <DndContext
      sensors={sensors}
      collisionDetection={closestCenter}
      onDragStart={handleDragStart}
      onDragOver={handleDragOver}
      onDragEnd={handleDragEnd}
    >
      <div className="grid grid-cols-4 gap-4 p-4">
        {Object.entries(columns).map(([columnId, items]) => (
          <Column
            key={columnId}
            id={columnId}
            title={columnTitles[columnId]}
            items={items}
            isOver={overColumnId === columnId}
            activeId={activeId}
          />
        ))}
      </div>
      <DragOverlay dropAnimation={null}>
        {activeCard ? (
          <SortableCard
            id={activeCard.id}
            title={activeCard.title}
            rating={activeCard.rating}
            isOverlay
          />
        ) : null}
      </DragOverlay>
    </DndContext>
  );
}

function Column({ id, title, items, isOver, activeId }) {
  const { setNodeRef } = useDroppable({ id });
  return (
    <div
      ref={setNodeRef}
      className={`p-2 transition-colors rounded-xl min-h-[100px] ${isOver ? "bg-gray-200" : "bg-white"}`}
    >
      <h2 className="font-bold text-lg mb-2">{title}</h2>
      <SortableContext items={items.map((item) => item.id)} strategy={rectSortingStrategy}>
        <div className="flex flex-col gap-4">
          {items.map((item) => (
            <SortableCard
              key={item.id}
              id={item.id}
              title={item.title}
              rating={item.rating || 0}
              isPreview={item.id === activeId}
            />
          ))}
        </div>
      </SortableContext>
    </div>
  );
}

function SortableCard({ id, title, rating, isOverlay = false, isPreview = false }) {
  const sortable = useSortable({ id });
  const { attributes, listeners, setNodeRef, transform, transition, isDragging } = sortable;

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    opacity: isDragging && !isOverlay ? 0.3 : 1,
    zIndex: isOverlay ? 999 : undefined,
    border: isPreview ? "2px dashed #aaa" : undefined,
  };

  return (
    <div
      ref={setNodeRef}
      {...attributes}
      {...listeners}
      style={style}
      className="bg-white p-3 border hover:shadow-md rounded-2xl relative cursor-move group w-64"
    >
      <div className="font-semibold mb-1">{title}</div>
      <div className="flex items-center justify-between text-sm text-gray-600">
        <div className="flex items-center gap-1">
          {[...Array(3)].map((_, idx) => (
            idx < rating ? <FaStar key={idx} className="text-yellow-400" /> : <FaRegStar key={idx} />
          ))}
          <FaClock className="ml-2" />
        </div>
        <FaEdit className="opacity-0 group-hover:opacity-100 transition duration-200 cursor-pointer" />
      </div>
    </div>
  );
}