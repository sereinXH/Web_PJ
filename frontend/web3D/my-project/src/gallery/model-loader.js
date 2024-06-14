import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader.js";

export function loadModel(scene, callback) {
    let sceneModel;
    const loader = new GLTFLoader();
    loader.load('/gallery/mtv_vma_gallery_2016.glb', (gltf) => {
        sceneModel = gltf.scene;
        scene.add(sceneModel);
        sceneModel.traverse(child => {
            // console.log(`Child object: ${child.name || child.id}`, child);
            if (child.isMesh) {
                child.geometry.computeBoundingBox();
                // console.log(`BoundingBox for ${child.name || child.id}:`, child.geometry.boundingBox);
                child.geometry.boundingBox.applyMatrix4(child.matrixWorld);
            }
        });
        if (callback) callback(sceneModel);
    }, undefined, function (error) {
        console.error(error);
    });
}
