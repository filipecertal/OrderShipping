"use strict";
let camera, scene, renderer;
function onCommonWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
}
function animate() {
    requestAnimationFrame(animate);
    renderer.render(scene, camera);
}
function move_mesh(mesh, l, h, d, x = 0, y = 0, z = 0) {
    mesh.position.set((l / 2) + x, (h / 2) + y, (d / 2) + z);
}
function create_mesh(l, h, d, c, ce, o) {
    const group = new THREE.Group();
    const mesh = new THREE.Mesh(new THREE.BoxBufferGeometry(l, h, d), new THREE.MeshPhongMaterial( {color: c, transparent: true, opacity: o, side: THREE.DoubleSide, depthWrite: false } ));
    group.add(mesh);
    group.add(new THREE.LineSegments( new THREE.EdgesGeometry( mesh.geometry ), new THREE.MeshBasicMaterial( {color: ce, transparent: true, opacity: o} ) ));
    return group;
}
function init() {
    scene = new THREE.Scene();
    scene.background = new THREE.Color(0xFFFFFF);
    const group = new THREE.Group();
    const raw = data['raw'];
    const max_size = Math.max(raw.length, raw.height, raw.depth);
    let box = create_mesh(raw.length, raw.height, raw.depth, raw.color, raw.color_edge, .1);
    move_mesh(box, raw.length, raw.height, raw.depth);
    group.add(box);
    for (let item of data['items']) {
        box = create_mesh(item.length, item.height, item.depth, item.color, item.color_edge, 0.4);
        move_mesh(box, item.length, item.height, item.depth, item.x, item.y, item.z);
        group.add(box);
    }
    scene.add(group);
    if (data['axes']) { scene.add(new THREE.AxesHelper(max_size * 1.2)); }
    camera = new THREE.PerspectiveCamera(90, window.innerWidth / window.innerHeight, 0.01, max_size * 10);
    camera.position.set(max_size, max_size, max_size);
    renderer = new THREE.WebGLRenderer({ antialias: true,  alpha: true,  preserveDrawingBuffer: true});
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.render(scene, camera);
    document.body.appendChild(renderer.domElement);
    const controls = new THREE.OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;
    controls.dampingFactor = 0.25;
    controls.enableZoom = true;
    controls.enablePan = true;
    const light = new THREE.DirectionalLight(0xFFFFFF, .5);
    light.position.set(max_size, 0, max_size);
    light.shadow.mapSize.width = 1024;
    light.shadow.mapSize.height = 1024;
    scene.add(light);
    window.addEventListener("resize", onCommonWindowResize);
    animate();
}
init();