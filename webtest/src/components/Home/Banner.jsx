import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, EffectFade, Pagination } from "swiper/modules";
import { Link } from "react-router-dom";
import "swiper/css";
import "swiper/css/effect-fade";
import "swiper/css/pagination";

import Banner1 from "../../assets/banner1.jpg";
import Banner2 from "../../assets/banner2.jpg";
import Banner3 from "../../assets/banner3.jpg";

function Banner() {
  const banners = [Banner1, Banner2, Banner3];

  return (
    <div className="relative w-full h-[750px]">
      <Swiper
        modules={[Autoplay, EffectFade, Pagination]}
        autoplay={{ delay: 5000, disableOnInteraction: false }}
        loop={true}
        effect="fade"
        pagination={{
          clickable: true,
          bulletClass: "swiper-pagination-bullet",
          bulletActiveClass: "swiper-pagination-bullet-active"
        }}
        className="w-full h-full"
      >
        {banners.map((img, index) => (
          <SwiperSlide key={index}>
            <Link to="/store">
              <div
                className="w-full h-full bg-cover bg-center"
                style={{ backgroundImage: `url(${img})` }}
              />
            </Link>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}

export default Banner;
