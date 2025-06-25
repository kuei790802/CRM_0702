import React, { useRef, useState } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, useGLTF } from '@react-three/drei';


function Model({ url }) {
  const gltf = useGLTF(url);
  const ref = useRef();
  const [rotate, setRotate] = useState(false);
  let rotationAmount = 0;

  useFrame(() => {
    if (rotate && ref.current) {
      const step = 0.05;
      if (rotationAmount < Math.PI * 2) {
        ref.current.rotation.y += step;
        rotationAmount += step;
      } else {
        setRotate(false); // 停止旋轉
        rotationAmount = 0;
      }
    }
  });

  return (
    <primitive
      object={gltf.scene}
      ref={ref}
      onClick={() => setRotate(true)}
      scale={1}
    />
  );
}

export default function ModelViewer() {
  return (
    <Canvas style={{ height: '500px' }}>
      <ambientLight />
      <pointLight position={[10, 10, 10]} />
      <Model url="/images/ice.glb" />
      <OrbitControls enableZoom={false} enableRotate={false} />
    </Canvas>
  );
}
