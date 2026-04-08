document.addEventListener("DOMContentLoaded", function () {
    const autoFocusTarget = document.querySelector("input[type='text'], textarea");
    if (autoFocusTarget) {
        autoFocusTarget.focus();
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const firstInput = document.querySelector("input");
    if (firstInput) {
        firstInput.focus();
    }
});