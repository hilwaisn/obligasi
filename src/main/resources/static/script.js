function numberFormat(nubmer) {
    const formattedCurrency = new Intl.NumberFormat('id-ID', {
        style: 'currency',
        currency: 'IDR'
    }).format(number);
    return formattedCurrency;  // "Rp1.234.567,89"
}