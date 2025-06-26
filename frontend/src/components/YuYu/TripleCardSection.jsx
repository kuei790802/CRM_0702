import React from 'react';
import { Link } from 'react-router-dom';

const cardData = [
  {
    title: 'Local Delivery',
    description: 'Our party packs are perfect for all occasions',
    image: '/images/062601.jpg',
    button: '前往購買',
    link: '/store'
  },
  {
    title: 'Hot Chocolate',
    description: 'Hot chocolatey goodness shipped to your doorstep',
    image: '/images/062602.jpg',
    button: '前往購買',
    link: '/store'
  },
  {
    title: 'Franchising',
    description: 'Bring Popbar to your city! Learn more about franchising',
    image: '/images/062603.jpg',
    button: '閱讀更多',
    link: '/About'
  }
];

const TripleCardSection = () => {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 p-4 md:p-10">
      {cardData.map((card, index) => (
        <Link
          to={card.link}
          key={index}
          className="relative group overflow-hidden rounded-2xl shadow-lg transform hover:scale-[1.02] transition duration-500"
        >
          <img
            src={card.image}
            alt={card.title}
            className="object-cover w-full h-[300px] sm:h-[350px] md:h-[600px] group-hover:scale-105 transition-transform duration-500 ease-in-out"
          />
          <div className="absolute top-[70%] left-0 right-0 bottom-0 bg-logo-lightBlue bg-opacity-40 group-hover:bg-opacity-30 transition duration-300" />
          <div className="absolute bottom-6 left-6 right-6 text-white z-10 animate-fadeIn">
            <h3 className="text-xl md:text-2xl font-bold uppercase tracking-wide animate-slideInUp">
              {card.title}
            </h3>
            <p className="mt-1 text-sm md:text-base max-w-[90%] animate-slideInUp delay-100">
              {card.description}
            </p>
            <div className="mt-4 animate-slideInUp delay-200">
              <span className="bg-white text-black px-4 py-2 rounded shadow hover:bg-gray-100 text-sm font-medium">
                {card.button}
              </span>
            </div>
          </div>
        </Link>
      ))}
    </div>
  );
};

export default TripleCardSection;

// Tailwind CSS animations (you can add this in your global CSS or Tailwind config if needed):
// .animate-fadeIn { animation: fadeIn 0.8s ease-in-out both; }
// .animate-slideInUp { animation: slideInUp 0.6s ease-out both; }
// @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
// @keyframes slideInUp { from { transform: translateY(20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
