const API_URL = "http://localhost:8000/api/devices";

// CADASTRAR UM DISPOSITIVO
// Recebe os dados do formulário (ou usa o exemplo abaixo se nada for passado).
async function criarDevice(device = {
    name: "Sensor de temperatura",
    brand: "Intelbras",
    model: "IT-100",
    category: "Sensor de temperatura",
    power: 5.0,
    hoursPerDay: 24,
    daysPerMonth: 30,
    imageUrl: "https://example.com/sensor.jpg",
    ownerId: 1,
    residenceId: 1,
    memberIds: []
}) {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(device)
    });

    if (!response.ok) {
        throw new Error(`Erro ao criar dispositivo: ${response.status} - ${await response.text()}`);
    }

    const resultado = await response.json();
    console.log("Dispositivo criado:", resultado);

    return resultado;
}

// BUSCAR UM DISPOSITIVO PELO ID
async function buscarDevice(id) {
    const response = await fetch(`${API_URL}/${id}`);

    if (!response.ok) {
        throw new Error(`Erro ao buscar dispositivo: ${response.status} - ${await response.text()}`);
    }

    const device = await response.json();
    console.log("Dispositivo encontrado:", device);

    return device;
}

// LISTAR TODOS OS DISPOSITIVOS DE UMA RESIDÊNCIA
async function listarDevicesPorResidencia(residenceId) {
    const response = await fetch(
        `${API_URL}/residence/${residenceId}`
    );

    if (!response.ok) {
        throw new Error(`Erro ao listar dispositivos: ${response.status} - ${await response.text()}`);
    }

    const devices = await response.json();
    console.log("Dispositivos da residência:", devices);

    return devices;
}

// EXEMPLOS DE EXECUÇÃO (não roda mais sozinho: chame executar() no console se quiser testar)
async function executar() {
    try {
        await criarDevice();
        await buscarDevice(1);
        // await listarDevicesPorResidencia(1);
    } catch (error) {
        console.error("Falha na requisição:", error);
    }
}
