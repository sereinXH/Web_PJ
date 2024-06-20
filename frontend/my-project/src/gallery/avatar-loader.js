import * as THREE from 'three';
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader.js';

export function loadAvatar(scene, onLoad, avatarPath = '/avatar/cha1.fbx', animPath = null) {
    const loader = new FBXLoader();
    loader.load(avatarPath, function (object) {
        const avatar = object;
        avatar.scale.set(0.008, 0.008, 0.008); // 调整形象大小
        avatar.position.set(0, 0, 0); // 设置初始位置

        avatar.traverse(child => {
            if (child.isMesh) {
                child.geometry.computeBoundingBox();
                child.geometry.boundingBox.applyMatrix4(child.matrixWorld);
            }
        });

        scene.add(avatar);

        if (animPath) {
            const animLoader = new FBXLoader();
            animLoader.load(animPath, function (anim) {
                const mixer = new THREE.AnimationMixer(avatar);
                const action = mixer.clipAction(anim.animations[0]);
                action.timeScale = 0.1; // 设置播放速度
                action.play();

                if (onLoad) onLoad(avatar, mixer, action);
            });
        } else {
            if (onLoad) onLoad(avatar);
        }
    }, undefined, function (error) {
        console.error(error);
    });
}
