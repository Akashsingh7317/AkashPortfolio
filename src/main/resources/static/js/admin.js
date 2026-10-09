/*
====================================================
 Admin Panel JavaScript
====================================================
*/

"use strict";

/* ===========================================
   Sidebar Toggle
=========================================== */

const sidebar = document.querySelector(".sidebar");
const sidebarToggle = document.querySelector("#sidebarToggle");

if (sidebarToggle) {

    sidebarToggle.addEventListener("click", function () {

        sidebar.classList.toggle("collapsed");

    });

}

/* ===========================================
   Confirm Delete
=========================================== */

function confirmDelete(message = "Are you sure you want to delete this record?") {

    return confirm(message);

}

/* ===========================================
   Image Preview
=========================================== */

function previewImage(input, previewId) {

    const preview = document.getElementById(previewId);

    if (!preview) return;

    if (input.files && input.files[0]) {

        const reader = new FileReader();

        reader.onload = function (e) {

            preview.src = e.target.result;

        };

        reader.readAsDataURL(input.files[0]);

    }

}

/* ===========================================
   Multiple Images Preview
=========================================== */

function previewGallery(input, containerId) {

    const container = document.getElementById(containerId);

    if (!container) return;

    container.innerHTML = "";

    [...input.files].forEach(file => {

        const reader = new FileReader();

        reader.onload = function (e) {

            const img = document.createElement("img");

            img.src = e.target.result;

            img.style.width = "120px";
            img.style.height = "120px";
            img.style.objectFit = "cover";
            img.style.margin = "8px";
            img.style.borderRadius = "8px";
            img.classList.add("shadow");

            container.appendChild(img);

        };

        reader.readAsDataURL(file);

    });

}

/* ===========================================
   Video Preview
=========================================== */

function previewVideo(input, previewId) {

    const video = document.getElementById(previewId);

    if (!video) return;

    if (input.files && input.files[0]) {

        video.src = URL.createObjectURL(input.files[0]);

        video.load();

    }

}

/* ===========================================
   Password Toggle
=========================================== */

function togglePassword(id, button) {

    const input = document.getElementById(id);

    const icon = button.querySelector("i");

    if (input.type === "password") {

        input.type = "text";

        icon.className = "bi bi-eye-slash";

    } else {

        input.type = "password";

        icon.className = "bi bi-eye";

    }

}

/* ===========================================
   Password Strength
=========================================== */

const newPassword = document.getElementById("newPassword");

if (newPassword) {

    newPassword.addEventListener("keyup", function () {

        const bar = document.getElementById("strengthBar");

        const text = document.getElementById("strengthText");

        let score = 0;

        const value = this.value;

        if (value.length >= 8) score++;
        if (/[A-Z]/.test(value)) score++;
        if (/[a-z]/.test(value)) score++;
        if (/[0-9]/.test(value)) score++;
        if (/[^A-Za-z0-9]/.test(value)) score++;

        const colors = [
            "bg-danger",
            "bg-danger",
            "bg-warning",
            "bg-info",
            "bg-primary",
            "bg-success"
        ];

        const labels = [
            "Very Weak",
            "Weak",
            "Fair",
            "Good",
            "Strong",
            "Very Strong"
        ];

        bar.className = "progress-bar " + colors[score];

        bar.style.width = (score * 20) + "%";

        text.innerHTML = labels[score];

    });

}

/* ===========================================
   Live Percentage
=========================================== */

const percentage = document.getElementById("percentageRange");

if (percentage) {

    const value = document.getElementById("percentageValue");

    value.innerHTML = percentage.value + "%";

    percentage.addEventListener("input", function () {

        value.innerHTML = this.value + "%";

    });

}

/* ===========================================
   Icon Preview
=========================================== */

const iconInput = document.getElementById("iconInput");

if (iconInput) {

    iconInput.addEventListener("keyup", function () {

        const preview = document.getElementById("iconPreview");

        preview.className = this.value;

        preview.style.fontSize = "80px";

        preview.style.color = "#0d6efd";

    });

}

/* ===========================================
   Auto Close Alerts
=========================================== */

setTimeout(function () {

    document.querySelectorAll(".alert").forEach(function (alert) {

        alert.style.transition = "0.5s";

        alert.style.opacity = "0";

        setTimeout(function () {

            alert.remove();

        }, 500);

    });

}, 3000);

/* ===========================================
   Scroll To Top
=========================================== */

const scrollButton = document.getElementById("scrollTop");

if (scrollButton) {

    window.addEventListener("scroll", function () {

        if (window.scrollY > 300) {

            scrollButton.style.display = "block";

        } else {

            scrollButton.style.display = "none";

        }

    });

    scrollButton.addEventListener("click", function () {

        window.scrollTo({

            top: 0,

            behavior: "smooth"

        });

    });

}

/* ===========================================
   Current Date & Time
=========================================== */

const clock = document.getElementById("liveClock");

if (clock) {

    setInterval(function () {

        clock.innerHTML = new Date().toLocaleString();

    }, 1000);

}

/* ===========================================
   Loading Button
=========================================== */

document.querySelectorAll("form").forEach(function (form) {

    form.addEventListener("submit", function () {

        const btn = form.querySelector("button[type='submit']");

        if (btn) {

            btn.disabled = true;

            btn.innerHTML =
                "<span class='spinner-border spinner-border-sm'></span> Please Wait...";

        }

    });

});

/* ===========================================
   Bootstrap Tooltips
=========================================== */

const tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
);

tooltipTriggerList.map(function (tooltipTriggerEl) {

    return new bootstrap.Tooltip(tooltipTriggerEl);

});

/* ===========================================
   Bootstrap Popovers
=========================================== */

const popoverTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="popover"]')
);

popoverTriggerList.map(function (popoverTriggerEl) {

    return new bootstrap.Popover(popoverTriggerEl);

});

console.log("Admin Panel Loaded Successfully");