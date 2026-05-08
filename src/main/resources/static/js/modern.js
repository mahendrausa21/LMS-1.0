/* ========================================
   MODERN FRONTEND - JAVASCRIPT
   File Upload Handler, Form Validation, UI Interactions
   ======================================== */

// File Upload Handler
class ModernFileUpload {
    constructor(uploadZoneId, inputId, previewId, progressId) {
        this.uploadZone = document.getElementById(uploadZoneId);
        this.fileInput = document.getElementById(inputId);
        this.preview = document.getElementById(previewId);
        this.progressBar = document.getElementById(progressId);
        this.files = [];

        if (this.uploadZone) {
            this.setupEventListeners();
        }
    }

    setupEventListeners() {
        // Click to upload
        this.uploadZone.addEventListener('click', () => this.fileInput.click());

        // File input change
        this.fileInput.addEventListener('change', (e) => this.handleFileSelect(e.target.files));

        // Drag and drop
        this.uploadZone.addEventListener('dragover', (e) => this.handleDragOver(e));
        this.uploadZone.addEventListener('dragleave', (e) => this.handleDragLeave(e));
        this.uploadZone.addEventListener('drop', (e) => this.handleDrop(e));
    }

    handleDragOver(e) {
        e.preventDefault();
        e.stopPropagation();
        this.uploadZone.classList.add('dragover');
    }

    handleDragLeave(e) {
        e.preventDefault();
        e.stopPropagation();
        this.uploadZone.classList.remove('dragover');
    }

    handleDrop(e) {
        e.preventDefault();
        e.stopPropagation();
        this.uploadZone.classList.remove('dragover');
        this.handleFileSelect(e.dataTransfer.files);
    }

    handleFileSelect(fileList) {
        this.files = Array.from(fileList);
        this.displayPreview();
    }

    displayPreview() {
        if (this.files.length === 0) {
            this.preview.classList.remove('show');
            return;
        }

        let html = '';
        this.files.forEach((file, index) => {
            const fileExt = file.name.split('.').pop().toUpperCase().substring(0, 3);
            const fileSize = (file.size / 1024 / 1024).toFixed(2);
            
            html += `
                <div class="file-item">
                    <div class="file-item-info">
                        <div class="file-item-icon">${fileExt}</div>
                        <div class="file-item-details">
                            <div class="file-item-name">${file.name}</div>
                            <div class="file-item-size">${fileSize} MB</div>
                        </div>
                    </div>
                    <button type="button" class="file-item-remove" data-index="${index}">Remove</button>
                </div>
            `;
        });

        this.preview.innerHTML = html;
        this.preview.classList.add('show');

        // Remove file event listeners
        this.preview.querySelectorAll('.file-item-remove').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                const index = parseInt(btn.getAttribute('data-index'));
                this.files.splice(index, 1);
                this.displayPreview();
            });
        });
    }

    getFiles() {
        return this.files;
    }

    clear() {
        this.files = [];
        this.fileInput.value = '';
        this.preview.classList.remove('show');
    }
}

// Form Validation
class ModernFormValidator {
    constructor(formId) {
        this.form = document.getElementById(formId);
        this.errors = {};

        if (this.form) {
            this.setupValidation();
        }
    }

    setupValidation() {
        this.form.addEventListener('submit', (e) => {
            if (!this.validate()) {
                e.preventDefault();
                this.showErrors();
            }
        });

        // Real-time validation
        this.form.querySelectorAll('input, textarea, select').forEach(field => {
            field.addEventListener('blur', () => this.validateField(field));
        });
    }

    validate() {
        this.errors = {};
        const fields = this.form.querySelectorAll('[required]');

        fields.forEach(field => {
            this.validateField(field);
        });

        return Object.keys(this.errors).length === 0;
    }

    validateField(field) {
        const fieldName = field.name;
        const value = field.value.trim();

        // Check required
        if (field.hasAttribute('required') && !value) {
            this.errors[fieldName] = `${this.getFieldLabel(field)} is required`;
            this.showFieldError(field, this.errors[fieldName]);
            return;
        }

        // Email validation
        if (field.type === 'email' && value) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(value)) {
                this.errors[fieldName] = 'Please enter a valid email address';
                this.showFieldError(field, this.errors[fieldName]);
                return;
            }
        }

        // Min length
        const minLength = field.getAttribute('minlength');
        if (minLength && value.length < minLength) {
            this.errors[fieldName] = `${this.getFieldLabel(field)} must be at least ${minLength} characters`;
            this.showFieldError(field, this.errors[fieldName]);
            return;
        }

        // Clear error
        this.clearFieldError(field);
        delete this.errors[fieldName];
    }

    getFieldLabel(field) {
        const label = document.querySelector(`label[for="${field.id}"]`);
        return label ? label.textContent.replace(' *', '') : field.name;
    }

    showFieldError(field, message) {
        field.classList.add('error');
        let errorEl = field.parentElement.querySelector('.form-error');
        
        if (!errorEl) {
            errorEl = document.createElement('div');
            errorEl.className = 'form-error';
            field.parentElement.appendChild(errorEl);
        }

        errorEl.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${message}`;
    }

    clearFieldError(field) {
        field.classList.remove('error');
        const errorEl = field.parentElement.querySelector('.form-error');
        if (errorEl) {
            errorEl.remove();
        }
    }

    showErrors() {
        const alertHtml = `
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <div>Please fix the errors below before submitting.</div>
            </div>
        `;
        
        const existingAlert = this.form.parentElement.querySelector('.alert');
        if (existingAlert) {
            existingAlert.remove();
        }

        this.form.insertAdjacentHTML('beforebegin', alertHtml);
    }
}

// Alert Handler
class ModernAlert {
    static show(message, type = 'info', duration = 5000) {
        const id = 'alert-' + Date.now();
        const alertHtml = `
            <div id="${id}" class="alert alert-${type}">
                <i class="fas fa-${this.getIcon(type)}"></i>
                <div>${message}</div>
                <button class="alert-close" onclick="document.getElementById('${id}').remove()">
                    ×
                </button>
            </div>
        `;

        document.body.insertAdjacentHTML('afterbegin', alertHtml);

        if (duration) {
            setTimeout(() => {
                const alert = document.getElementById(id);
                if (alert) alert.remove();
            }, duration);
        }
    }

    static success(message) {
        this.show(message, 'success');
    }

    static error(message) {
        this.show(message, 'error', 7000);
    }

    static warning(message) {
        this.show(message, 'warning');
    }

    static info(message) {
        this.show(message, 'info');
    }

    static getIcon(type) {
        const icons = {
            success: 'check-circle',
            error: 'exclamation-circle',
            warning: 'exclamation-triangle',
            info: 'info-circle'
        };
        return icons[type] || 'info-circle';
    }
}

// Smooth Scroll
class SmoothScroll {
    static init() {
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function(e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
    }
}

// Loading State Management
class LoadingState {
    static setLoading(buttonId, isLoading = true) {
        const btn = document.getElementById(buttonId);
        if (!btn) return;

        if (isLoading) {
            btn.disabled = true;
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing...';
            btn.style.pointerEvents = 'none';
        } else {
            btn.disabled = false;
            btn.innerHTML = btn.getAttribute('data-original-text') || 'Submit';
            btn.style.pointerEvents = 'auto';
        }
    }

    static init(buttonId) {
        const btn = document.getElementById(buttonId);
        if (btn) {
            btn.setAttribute('data-original-text', btn.innerHTML);
        }
    }
}

// Modern Tabs
class ModernTabs {
    constructor(containerSelector) {
        this.container = document.querySelector(containerSelector);
        this.buttons = this.container.querySelectorAll('[data-tab]');
        this.panes = this.container.querySelectorAll('[data-pane]');

        this.buttons.forEach(btn => {
            btn.addEventListener('click', () => this.switchTab(btn.getAttribute('data-tab')));
        });
    }

    switchTab(tabName) {
        // Hide all panes
        this.panes.forEach(pane => {
            pane.style.display = 'none';
            pane.classList.remove('active');
        });

        // Remove active from buttons
        this.buttons.forEach(btn => btn.classList.remove('active'));

        // Show selected pane
        const pane = this.container.querySelector(`[data-pane="${tabName}"]`);
        if (pane) {
            pane.style.display = 'block';
            pane.classList.add('active');
            pane.scrollIntoView({ behavior: 'smooth' });
        }

        // Add active to button
        const btn = this.container.querySelector(`[data-tab="${tabName}"]`);
        if (btn) btn.classList.add('active');
    }
}

// Modern Modal
class ModernModal {
    constructor(modalId) {
        this.modal = document.getElementById(modalId);
        this.setupEventListeners();
    }

    setupEventListeners() {
        // Close on X button
        const closeBtn = this.modal.querySelector('[data-close]');
        if (closeBtn) {
            closeBtn.addEventListener('click', () => this.close());
        }

        // Close on backdrop click
        this.modal.addEventListener('click', (e) => {
            if (e.target === this.modal) {
                this.close();
            }
        });
    }

    open() {
        this.modal.style.display = 'flex';
        this.modal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    close() {
        this.modal.style.display = 'none';
        this.modal.classList.remove('active');
        document.body.style.overflow = 'auto';
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    // Initialize smooth scroll
    SmoothScroll.init();

    // Initialize all modals
    document.querySelectorAll('[data-modal]').forEach(modal => {
        const modalId = modal.getAttribute('data-modal');
        modal.addEventListener('click', () => {
            const m = new ModernModal(modalId);
            m.open();
        });
    });

    // Initialize all buttons with loading state
    document.querySelectorAll('[data-loading]').forEach(btn => {
        LoadingState.init(btn.id);
    });
});

// Helper function to submit form with AJAX
async function submitFormWithAjax(formId, endpoint) {
    const form = document.getElementById(formId);
    const validator = new ModernFormValidator(formId);

    if (!validator.validate()) {
        validator.showErrors();
        return;
    }

    try {
        LoadingState.setLoading('submit-btn', true);

        const formData = new FormData(form);
        const response = await fetch(endpoint, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            ModernAlert.success('Operation completed successfully!');
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        } else {
            const data = await response.json();
            ModernAlert.error(data.message || 'An error occurred. Please try again.');
            LoadingState.setLoading('submit-btn', false);
        }
    } catch (error) {
        ModernAlert.error('Network error. Please try again.');
        LoadingState.setLoading('submit-btn', false);
    }
}

// Export for use in other scripts
window.ModernFileUpload = ModernFileUpload;
window.ModernFormValidator = ModernFormValidator;
window.ModernAlert = ModernAlert;
window.ModernModal = ModernModal;
window.LoadingState = LoadingState;
window.ModernTabs = ModernTabs;
window.SmoothScroll = SmoothScroll;
window.submitFormWithAjax = submitFormWithAjax;
