import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay } from 'swiper/modules';
import 'swiper/css';

function TopCarouselBar() {
  const messages = [
    "📢 全館滿千折百🔥 限時優惠中！",
    "🧧 訂閱電子報享 9 折優惠，立即註冊",
    "🎁 新會員註冊即贈購物金 $100",
  ];

  return (
    <div className="w-full bg-white overflow-hidden py-1 px-4">
      <Swiper
        direction="vertical"
        modules={[Autoplay]}
        autoplay={{
          delay: 5000,
          disableOnInteraction: false,
        }}
        speed={1200}
        loop={true}
        slidesPerView={1}
        allowTouchMove={false}
        className="h-[33px]"
      >
        {messages.map((msg, index) => (
          <SwiperSlide
            key={index}
          >
           <div className="flex items-center justify-center h-[40px]">
              <p className="text-sm text-gray-800 font-medium text-center">
                {msg}
              </p>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}

export default TopCarouselBar;
