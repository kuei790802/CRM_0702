import { create } from 'zustand';

const useCartStore = create((set, get) => ({
  // 購物車狀態
  isCartOpen: false,
  items: [],
  
  // 開啟/關閉購物車
  openCart: () => set({ isCartOpen: true }),
  closeCart: () => set({ isCartOpen: false }),
  toggleCart: () => set((state) => ({ isCartOpen: !state.isCartOpen })),
  
  // 添加商品到購物車
  addItem: (product) => set((state) => {
    const existingItem = state.items.find(item => item.id === product.id);
    
    if (existingItem) {
      return {
        items: state.items.map(item =>
          item.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      };
    } else {
      return {
        items: [...state.items, { ...product, quantity: 1 }]
      };
    }
  }),
  
  // 移除商品
  removeItem: (productId) => set((state) => ({
    items: state.items.filter(item => item.id !== productId)
  })),
  
  // 更新商品數量
  updateQuantity: (productId, quantity) => set((state) => {
    if (quantity <= 0) {
      return {
        items: state.items.filter(item => item.id !== productId)
      };
    }
    
    return {
      items: state.items.map(item =>
        item.id === productId
          ? { ...item, quantity }
          : item
      )
    };
  }),
  
  // 清空購物車
  clearCart: () => set({ items: [] }),
  
  // 計算總數量
  getTotalQuantity: () => {
    const { items } = get();
    return items.reduce((total, item) => total + item.quantity, 0);
  },
  
  // 計算總金額
  getTotalPrice: () => {
    const { items } = get();
    return items.reduce((total, item) => total + (item.price * item.quantity), 0);
  }
}));

export default useCartStore;
