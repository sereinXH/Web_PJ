import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';

export function loadAvatar(scene, onLoad, avatarPath = '/avatar/Furina.glb') {
    const loader = new GLTFLoader();
    loader.load(avatarPath, function(gltf) {
        const avatar = gltf.scene;
        avatar.scale.set(0.8, 0.8, 0.8); // 调整形象大小
        avatar.position.set(0, 0, 0); // 设置初始位置

        avatar.traverse(child => {
            if (child.isMesh) {
                child.geometry.computeBoundingBox();
                child.geometry.boundingBox.applyMatrix4(child.matrixWorld);
            }
        });

        scene.add(avatar);
        if (onLoad) onLoad(avatar);
    }, undefined, function(error) {
        console.error(error);
    });
}
