const sidebarToggle = document.querySelector("[data-sidebar-toggle]");
const appShell = document.querySelector(".app-shell");

if (sidebarToggle && appShell) {
    sidebarToggle.addEventListener("click", () => {
        appShell.classList.toggle("sidebar-collapsed");
    });
}

document.querySelectorAll(".toast").forEach((toastElement) => {
    if (window.bootstrap) {
        new bootstrap.Toast(toastElement, { delay: 3600 }).show();
    }
});

const deleteModal = document.getElementById("deleteStudentModal");

if (deleteModal && window.bootstrap) {
    const modal = new bootstrap.Modal(deleteModal);
    const deleteForm = deleteModal.querySelector("[data-delete-form]");
    const deleteName = deleteModal.querySelector("[data-delete-name]");
    const deleteRoll = deleteModal.querySelector("[data-delete-roll]");

    document.querySelectorAll("[data-delete-trigger]").forEach((button) => {
        button.addEventListener("click", () => {
            if (deleteForm) {
                deleteForm.action = button.dataset.deleteUrl;
            }

            if (deleteName) {
                deleteName.textContent = button.dataset.studentName || "Selected student";
            }

            if (deleteRoll) {
                deleteRoll.textContent = button.dataset.rollNo ? `Roll No: ${button.dataset.rollNo}` : "Roll No not available";
            }

            modal.show();
        });
    });
}
