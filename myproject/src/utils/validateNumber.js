function validateNumber(value) {
    let reg = new RegExp( "/^[0-9]*$")
    if (!reg.test(value)) {
        alert("请输入有效数字!");
        return false;
    }
    if (value > 100 || value < 1) {
        alert("请1值100之间的数字!");
        return false;
    }
}

export default validateNumber;