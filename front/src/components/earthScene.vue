<template>
  <div ref="container" id="container"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';

export default defineComponent({
  setup() {
    const container = ref<HTMLDivElement | null>(null);

    onMounted(() => {
      initScene();
    });

    const initScene = () => {
      if (!container.value) return;

      const scene = new THREE.Scene();
      const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
      const renderer = new THREE.WebGLRenderer();
      renderer.setSize(window.innerWidth, window.innerHeight);
      renderer.setClearColor(0x000000, 0); // 设置透明背景
      container.value.appendChild(renderer.domElement);

      // 添加环境光和方向光
      const ambientLight = new THREE.AmbientLight(0xfffff2, 0.4); // 创建环境光
      scene.add(ambientLight);
      scene.background = null;

      
      // 加载纹理
      const textureLoader = new THREE.TextureLoader();
      const albedoTexture = textureLoader.load('/textures/earth albedo.jpg');
      const cloudsTexture = textureLoader.load('/textures/earth clouds.png');
      //const bumpTexture = textureLoader.load('/textures/earth bump.jpg');
      //const oceanTexture = textureLoader.load('/textures/earth ocean.ppg');
      const nightTexture = textureLoader.load('/textures/earth night.png');

      // const earthMaterial = new THREE.MeshStandardMaterial({
      //   map: albedoTexture,
      //   alphaMap: cloudsTexture,
      //   transparent: true,
      //   alphaTest: 0.5,
      //   emissiveMap: nightTexture,
      //   emissive: new THREE.Color(0xF5C80E),
      //   //bumpMap: bumpTexture, 
      //   //加了上面这个会有点卡……
      //   //metalnessMap: oceanTexture,
      // }); 

      const baseMaterial = new THREE.MeshStandardMaterial({
        map: albedoTexture,
        emissiveMap: nightTexture,
        emissive: new THREE.Color(0xF5C80E),
      });

      // 创建云层材质
      const cloudMaterial = new THREE.MeshStandardMaterial({
        map: cloudsTexture,
        transparent: true,
        opacity: 0.4
      });

      // 加载地球模型
      const loader = new GLTFLoader();
      loader.load(
        '/earth.gltf',
        (gltf: any) => {
          const earth = gltf.scene;
          
          earth.traverse((child:any) => {
            if (child.isMesh) {
              // 创建地球网格
              const earthMesh = new THREE.Mesh(child.geometry, baseMaterial);
              
              // 创建云层网格，稍大于地球网格
              const cloudMesh = new THREE.Mesh(child.geometry, cloudMaterial);
              cloudMesh.scale.set(1.01, 1.01, 1.01);

              // 创建一个组将地球和云层组合在一起
              const earthGroup = new THREE.Group();
              earthGroup.add(earthMesh);
              earthGroup.add(cloudMesh);

              // 将组合后的模型添加到场景中
              scene.add(earthGroup);

              // 设置地球的初始位置和大小
              earthGroup.position.set(0, -40, 0);
              earthGroup.scale.set(5.5, 5.5, 5.5);
              
              // 添加鼠标移动事件监听
              document.addEventListener('mousemove', (event) => {
                const mouseX = (event.clientX / window.innerWidth) * 2 - 1;
                const mouseY = -(event.clientY / window.innerHeight) * 2 + 1;
                const earthX = 1.9;
                const earthY = 3.3;
                // 根据鼠标位置旋转地球
                earthGroup.rotation.x = earthX + mouseY * 0.05;
                earthGroup.rotation.y = earthY + mouseX * 0.05;
                earthGroup.rotation.z = 6;
              });
            }
          });

          // 渲染场景
          const animate = () => {
            requestAnimationFrame(animate);
            renderer.render(scene, camera);
          };
          animate();
        },
        undefined,
        (error: any) => {
          console.error(error);
        }
      );

      // 设置相机位置
      camera.position.z = 500;
    };

    return {
      container,
    };
  },
});
</script>

<style scoped>
</style>
