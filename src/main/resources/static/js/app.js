/**
 * 
 */document.addEventListener('DOMContentLoaded', function() {
    // Example: Form validation
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Example: Modal for confirmations
    const modalTrigger = document.querySelectorAll('.modal-trigger');
    modalTrigger.forEach(btn => {
        btn.addEventListener('click', () => {
            const myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
            myModal.show();
        });
    });
});