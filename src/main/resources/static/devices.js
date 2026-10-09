
const API_URL = "http://localhost:8000/api/devices";

// CADASTRAR UM DISPOSITIVO
async function criarDevice() {
    const device = {
        name: "Sensor de temperatura",
        source: "Amazon",
        price: 99.90,
        originalPrice: 129.90,
        imageUrl: "https://example.com/sensor.jpg",
        deviceUrl: "https://example.com/produto",
        power: 5.0,
        brand: "Intelbras",
        model: "IT-100",
        category: "Sensor de temperatura",
        tariff: 0.8126,
        residenceId: 1
    };

    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(device)
    });

    if (!response.ok) {
        throw new Error(`Erro ao criar dispositivo: ${response.status}`);
    }

    const resultado = await response.json();
    console.log("Dispositivo criado:", resultado);

    return resultado;
}

// BUSCAR UM DISPOSITIVO PELO ID
async function buscarDevice(id) {
    const response = await fetch(`${API_URL}/${id}`);

    if (!response.ok) {
        throw new Error(`Erro ao buscar dispositivo: ${response.status}`);
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
        throw new Error(`Erro ao listar dispositivos: ${response.status}`);
    }

    const devices = await response.json();
    console.log("Dispositivos da residência:", devices);

    return devices;
}

// EXEMPLOS DE EXECUÇÃO
async function executar() {
    try {
        // Cadastra um dispositivo
        await criarDevice();

        await buscarDevice(1);

        // await listarDevicesPorResidencia(1);
    } catch (error) {
        console.error("Falha na requisição:", error);
    }
}

executar();