const BASE_URL = 'http://localhost:8080/api/v1';

export async function getAllProducts() {
    const response = await fetch(`${BASE_URL}/products`);
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.errorMsg || 'Could not fetch products.');
    }

    return data.result;
}

export async function getPriceList(requestParams) {
    const response = await fetch(`${BASE_URL}/products/${requestParams.productId}/price-list?for=${requestParams.noOfUnits}`);
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.errorMsg || 'Could not fetch pricelist.');
    }

    return data.result;
}

export async function getCartons(productId) {
    const response = await fetch(`${BASE_URL}/products/${productId}/cartons`);
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.errorMsg || 'Could not fetch product cartons.');
    }

    return data.result;
}

export async function getUnitsPrice(requestParams) {
    const response = await fetch(`${BASE_URL}/products/${requestParams.productId}/prices?noOfUnits=${requestParams.noOfUnits}`);
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.errorMsg || 'Could not get the price.');
    }

    return data.result;
}

export async function getCartonsPrice(requestParams) {
    const response = await fetch(`${BASE_URL}/products/cartons/${requestParams.cartonId}/prices?noOfCartons=${requestParams.noOfCartons}`);
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.errorMsg || 'Could not get the price.');
    }

    return data.result;
}
