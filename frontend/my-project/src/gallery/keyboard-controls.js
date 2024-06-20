export function setupKeyboardControls(camera) {
    const moveSpeed = 0.1;
    const keys = {
        ArrowUp: false,
        ArrowDown: false,
        ArrowLeft: false,
        ArrowRight: false,
        w: false,
        s: false,
        a: false,
        d: false
    };

    function handleKeyDown(event) {
        if (keys.hasOwnProperty(event.key)) {
            keys[event.key] = true;
        }
    }

    function handleKeyUp(event) {
        if (keys.hasOwnProperty(event.key)) {
            keys[event.key] = false;
        }
    }

    function updateCameraPosition() {
        if (keys.ArrowUp || keys.w) camera.translateZ(-moveSpeed);
        if (keys.ArrowDown || keys.s) camera.translateZ(moveSpeed);
        if (keys.ArrowLeft || keys.a) camera.translateX(-moveSpeed);
        if (keys.ArrowRight || keys.d) camera.translateX(moveSpeed);
    }

    window.addEventListener('keydown', handleKeyDown);
    window.addEventListener('keyup', handleKeyUp);

    return updateCameraPosition;
}
