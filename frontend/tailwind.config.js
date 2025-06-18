/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        logo: {
          lightBlue: '#9DCCF3', // 淺藍色
          blue: '#6DA1CD',      // 較深藍色
          tan: '#f5f5f7',       // 淡棕黃色
        },
      },
    },
    container: {
      center: true,
      padding: {
        DEFAULT: '1rem', // base padding
        sm: '1.5rem',
        lg: '2rem',
        xl: '2.5rem',
        '2xl': '4rem',
      },
      screens: {
        sm: '640px',
        md: '768px',
        lg: '1024px',
        xl: '1280px',
        '2xl': '1536px',
      },
    },
  },
  plugins: [],
}
