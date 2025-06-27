// BottomCarousel.jsx
export default function BottomCarousel({ images }) {
  return (
    <div className="group relative overflow-hidden h-[220px]">
      <div className="flex w-max gap-6 animate-scroll-left group-hover:[animation-play-state:paused]">
        {[...images, ...images].map((src, i) => (
          <div
            key={`bottom-${i}`}
            className="w-[350px] h-[220px] rounded-xl overflow-hidden shrink-0 shadow-md"
          >
            <img
              src={src}
              alt={`bottom-img-${i}`}
              className="w-full h-full object-cover"
            />
          </div>
        ))}
      </div>
    </div>
  );
}
