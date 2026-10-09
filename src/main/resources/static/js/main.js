// ======================================
// LOADER
// ======================================

window.addEventListener("load", function () {

    const loader = document.getElementById("loader");

    if (loader) {

        loader.style.opacity = "0";

        setTimeout(() => {

            loader.style.display = "none";

        }, 500);

    }

});

// ======================================
// STICKY NAVBAR
// ======================================

const navbar = document.querySelector(".glass-navbar");

window.addEventListener("scroll", function () {

    if (window.scrollY > 50) {

        navbar.classList.add("shadow");

    } else {

        navbar.classList.remove("shadow");

    }

});

// ======================================
// SCROLL TO TOP
// ======================================

const scrollButton = document.getElementById("scrollTop");

window.addEventListener("scroll", () => {

    if (window.scrollY > 300) {

        scrollButton.classList.add("show");

    } else {

        scrollButton.classList.remove("show");

    }

});

scrollButton.addEventListener("click", () => {

    window.scrollTo({

        top: 0,

        behavior: "smooth"

    });

});

// ======================================
// DARK MODE
// ======================================

const themeButton = document.getElementById("themeToggle");

if (localStorage.getItem("theme") === "dark") {

    document.body.classList.add("dark-mode");

}

themeButton.addEventListener("click", () => {

    document.body.classList.toggle("dark-mode");

    if (document.body.classList.contains("dark-mode")) {

        localStorage.setItem("theme", "dark");

    } else {

        localStorage.removeItem("theme");

    }

});

// ======================================
// ACTIVE NAVIGATION
// ======================================

const sections = document.querySelectorAll("section");

const navLinks = document.querySelectorAll(".navbar .nav-link");

window.addEventListener("scroll", () => {

    let current = "";

    sections.forEach(section => {

        const sectionTop = section.offsetTop - 150;

        const sectionHeight = section.clientHeight;

        if (pageYOffset >= sectionTop) {

            current = section.getAttribute("id");

        }

    });

    navLinks.forEach(link => {

        link.classList.remove("active");

        if (link.getAttribute("href") === "#" + current) {

            link.classList.add("active");

        }

    });

});

// ======================================
// SMOOTH SCROLL
// ======================================

document.querySelectorAll('a[href^="#"]').forEach(anchor => {

    anchor.addEventListener("click", function (e) {

        e.preventDefault();

        document.querySelector(this.getAttribute("href"))

            .scrollIntoView({

                behavior: "smooth"

            });

    });

});

// ======================================
// COUNTER ANIMATION
// ======================================

const counters = document.querySelectorAll(".counter");

const runCounter = () => {

    counters.forEach(counter => {

        const target = parseInt(counter.innerText);

        let count = 0;

        const speed = target / 100;

        const update = () => {

            if (count < target) {

                count += speed;

                counter.innerText = Math.ceil(count) + "+";

                requestAnimationFrame(update);

            } else {

                counter.innerText = target + "+";

            }

        };

        update();

    });

};

const counterObserver = new IntersectionObserver(entries => {

    entries.forEach(entry => {

        if (entry.isIntersecting) {

            runCounter();

            counterObserver.disconnect();

        }

    });

});

if (document.querySelector(".counter")) {

    counterObserver.observe(document.querySelector(".counter"));

}

// ======================================
// PROGRESS BAR ANIMATION
// ======================================

const progressBars = document.querySelectorAll(".progress-bar");

const progressObserver = new IntersectionObserver(entries => {

    entries.forEach(entry => {

        if (entry.isIntersecting) {

            const bar = entry.target;

            const width = bar.style.width;

            bar.style.width = "0";

            setTimeout(() => {

                bar.style.width = width;

            }, 200);

        }

    });

});

progressBars.forEach(bar => progressObserver.observe(bar));

// ======================================
// CONTACT FORM VALIDATION
// ======================================

const form = document.querySelector("form");

if (form) {

    form.addEventListener("submit", function (e) {

        const name = form.querySelector("input[name='name']");
        const email = form.querySelector("input[name='email']");
        const subject = form.querySelector("input[name='subject']");
        const message = form.querySelector("textarea[name='message']");

        if (

            name.value.trim() === "" ||

            email.value.trim() === "" ||

            subject.value.trim() === "" ||

            message.value.trim() === ""

        ) {

            e.preventDefault();

            alert("Please fill all required fields.");

        }

    });

}

// ======================================
// MOBILE MENU
// ======================================

document.querySelectorAll(".navbar-collapse a")

.forEach(link => {

    link.addEventListener("click", () => {

        const menu = document.querySelector(".navbar-collapse");

        menu.classList.remove("show");

    });

});

// ======================================
// IMAGE LAZY LOADING
// ======================================

document.querySelectorAll("img").forEach(image => {

    image.loading = "lazy";

});

// ======================================
// PROJECT CARD HOVER
// ======================================

document.querySelectorAll(".project-card")

.forEach(card => {

    card.addEventListener("mouseenter", () => {

        card.classList.add("shadow-blue");

    });

    card.addEventListener("mouseleave", () => {

        card.classList.remove("shadow-blue");

    });

});

// ======================================
// PARALLAX HERO
// ======================================

window.addEventListener("scroll", () => {

    const hero = document.querySelector(".hero-section");

    if (hero) {

        hero.style.backgroundPositionY =

                window.scrollY * 0.3 + "px";

    }

});

// ======================================
// PAGE VISIT
// ======================================

console.log("Portfolio Loaded Successfully");

// ======================================
// END
// ======================================