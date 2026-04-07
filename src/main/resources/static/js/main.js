document.addEventListener("DOMContentLoaded", function () {
    const autoFocusTarget = document.querySelector("input[type='text'], textarea");
    if (autoFocusTarget) {
        autoFocusTarget.focus();
    }
});