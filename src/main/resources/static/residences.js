const API_URL = "http://localhost:8000/api/residences";

// CADASTRAR UMA RESIDÊNCIA
// Recebe os dados do formulário (ou usa o exemplo abaixo se nada for passado).
async function criarResidence(residence = {
    name: "Residência de teste",
    address: "Rua 31241 Jaguariuna",
    tariff: 0.8126,
    ownerId: 1,
    monthlyKwhLimit: 500,
    monthlyCostLimit: 406.30,
    alertThresholdPercent: 80
}) {
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
        throw new Error(`Erro ao buscar residência: ${response.status} - ${await response.text()}`);
    }

    const residence = await response.json();
    console.log("Residência encontrada:", residence);
    return residence;
}

// LISTAR RESIDÊNCIAS DE UM USUÁRIO
// Rota do controller: GET /api/residences?userId={id}
async function listarResidencesPorUser(userId) {
    const response = await fetch(`${API_URL}?userId=${userId}`);

    if (!response.ok) {
        throw new Error(`Erro ao listar residências: ${response.status} - ${await response.text()}`);
    }

    const residences = await response.json();
    console.log("Residências do usuário:", residences);
    return residences;
}

// EXEMPLOS DE EXECUÇÃO (não roda mais sozinho: chame executar() no console se quiser testar)
async function executar() {
    try {
        await criarResidence();
        await buscarResidence(1);
        // await listarResidencesPorUser(1);
    } catch (error) {
        console.error("Falha na requisição:", error);
    }
}
