
const API_URL = "http://localhost:8000/api/residences";

// CADASTRAR UMA RESIDÊNCIA
async function criarResidence() {
    const residence = {
        name: "Residência de teste",
        address: "Rua 31241 Jaguariuna",
        tariff: 0.8126,
        monthlyKwhLimit: 500,
        monthlyCostLimit: 406.30,
        alertThresholdPercent:80,
        ownerId: 3,
        createdAt: "2026-10-08T20:00:00",
        totalKwh: 100.0,
        totalCost: 81.26,
        activeMember: []
    };

    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(residence)
    });

    if (!response.ok) {
        throw new Error(`Erro ao criar residência: ${response.status} - ${await response.text()}`);
    }

    const resultado = await response.json();
    console.log("Residência criada:", resultado);
    return resultado;
}

// BUSCAR UMA RESIDÊNCIA PELO ID
async function buscarResidence(id) {
    const response = await fetch(`${API_URL}/${id}`);

    if (!response.ok) {
        throw new Error(`Erro ao buscar residência: ${response.status}`);
    }

    const residence = await response.json();
    console.log("Residência encontrada:", residence);
    return residence;
}

// LISTAR RESIDÊNCIAS DE UM MEMBRO
// Esta rota é um exemplo: depende de existir no seu controller.
async function listarResidencesPorMember(memberId) {
    const response = await fetch(`${API_URL}/member/${memberId}`);

    if (!response.ok) {
        throw new Error(`Erro ao listar residências: ${response.status}`);
    }

    const residences = await response.json();
    console.log("Residências do membro:", residences);
    return residences;
}

async function executar() {
    try {
        await criarResidence();
        await buscarResidence(1);
        // await listarResidencesPorMember(1);
    } catch (error) {
        console.error("Falha na requisição:", error);
    }
}

executar();
