/* Akash Singh — Portfolio scripts (no dependencies) */
(function () {
    "use strict";

    var reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

    /* ---------- Loader (first visit per browser session) ---------- */
    var loader = document.getElementById("loader");
    if (loader) {
        var seen = false;
        try { seen = sessionStorage.getItem("loaderSeen") === "1"; } catch (e) { /* ignore */ }

        var finish = function () {
            loader.classList.add("done");
            document.body.classList.remove("is-loading");
            try { sessionStorage.setItem("loaderSeen", "1"); } catch (e) { /* ignore */ }
        };

        if (seen || reduceMotion) {
            finish();
        } else {
            document.body.classList.add("is-loading");
            var pct = document.getElementById("loaderPct");
            var bar = document.getElementById("loaderBar");
            var start = performance.now(), duration = 1700;
            (function step(now) {
                var t = Math.min((now - start) / duration, 1);
                var eased = 1 - Math.pow(1 - t, 3);
                var v = Math.round(eased * 100);
                if (pct) pct.textContent = v;
                if (bar) bar.style.width = v + "%";
                if (t < 1) requestAnimationFrame(step);
                else setTimeout(finish, 250);
            })(start);
        }
    }

    /* ---------- Mobile menu ---------- */
    var menuBtn = document.getElementById("menuToggle");
    var navLinks = document.getElementById("navLinks");
    if (menuBtn && navLinks) {
        menuBtn.addEventListener("click", function () {
            navLinks.classList.toggle("open");
        });
        navLinks.addEventListener("click", function (e) {
            if (e.target.tagName === "A") navLinks.classList.remove("open");
        });
    }

    /* ---------- Scroll effects: nav, progress bar, hero fade ---------- */
    var nav = document.getElementById("nav");
    var progress = document.getElementById("progress");
    var heroInner = document.getElementById("heroInner");
    var ticking = false;

    function onScroll() {
        var y = window.scrollY;
        if (nav) nav.classList.toggle("scrolled", y > 16 || !heroInner);
        if (progress) {
            var max = document.documentElement.scrollHeight - window.innerHeight;
            progress.style.transform = "scaleX(" + (max > 0 ? Math.min(y / max, 1) : 0) + ")";
        }
        if (heroInner && !reduceMotion) {
            // The hero recedes as the profile section scrolls over it
            var p = Math.min(y / window.innerHeight, 1);
            heroInner.style.transform = "translateY(" + (p * 18) + "%) scale(" + (1 - p * 0.12) + ")";
            heroInner.style.opacity = String(1 - p * 0.9);
        }
        ticking = false;
    }
    window.addEventListener("scroll", function () {
        if (!ticking) { ticking = true; requestAnimationFrame(onScroll); }
    }, { passive: true });
    onScroll();

    /* ---------- Highlight the nav link of the visible section ---------- */
    if ("IntersectionObserver" in window && navLinks && document.getElementById("home")) {
        var linkFor = {};
        navLinks.querySelectorAll('a[href*="#"]').forEach(function (a) {
            linkFor[a.getAttribute("href").split("#")[1]] = a;
        });
        var sectionObserver = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (!entry.isIntersecting) return;
                navLinks.querySelectorAll("a.active").forEach(function (a) { a.classList.remove("active"); });
                var link = linkFor[entry.target.id];
                if (link) link.classList.add("active");
            });
        }, { rootMargin: "-45% 0px -50% 0px" });
        document.querySelectorAll("main section[id]").forEach(function (s) { sectionObserver.observe(s); });
    }

    /* ---------- Reveal on scroll ---------- */
    (function () {
        var items = document.querySelectorAll(".reveal:not(.visible)");
        if (reduceMotion || !("IntersectionObserver" in window)) {
            items.forEach(function (el) { el.classList.add("visible"); });
            return;
        }
        var io = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add("visible");
                    io.unobserve(entry.target);
                }
            });
        }, { threshold: 0.08, rootMargin: "0px 0px -40px 0px" });
        items.forEach(function (el) { io.observe(el); });
    })();

    /* ---------- Hero: rotating headline ---------- */
    var words = document.querySelectorAll(".hero-word");
    if (words.length > 1 && !reduceMotion) {
        var current = 0;
        setInterval(function () {
            var prev = words[current];
            current = (current + 1) % words.length;
            prev.classList.remove("on");
            prev.classList.add("off");
            words[current].classList.remove("off");
            words[current].classList.add("on");
            setTimeout(function () { prev.classList.remove("off"); }, 650);
        }, 2600);
    }

    /* ---------- Projects: 3D coverflow ---------- */
    var track = document.getElementById("carouselTrack");
    if (track) {
        var cards = Array.prototype.slice.call(track.querySelectorAll(".project-card"));
        var n = cards.length;
        var dotsWrap = document.getElementById("carouselDots");
        var prevBtn = document.getElementById("carouselPrev");
        var nextBtn = document.getElementById("carouselNext");
        var countEl = document.getElementById("carouselCount");
        var active = 0;
        var pad2 = function (v) { return (v < 10 ? "0" : "") + v; };

        var dots = cards.map(function (card, i) {
            var b = document.createElement("button");
            b.type = "button";
            b.setAttribute("aria-label", "Show project " + (i + 1));
            b.addEventListener("click", function () { goTo(i); });
            if (dotsWrap) dotsWrap.appendChild(b);
            return b;
        });

        function layout() {
            var w = cards[0].offsetWidth;
            var step = Math.min(w * 0.66, window.innerWidth * 0.32);
            cards.forEach(function (card, i) {
                // Offset from the active card, wrapped so the row loops
                var o = i - active;
                if (n > 2) {
                    if (o > n / 2) o -= n;
                    if (o < -n / 2) o += n;
                }
                var a = Math.abs(o), s = o < 0 ? -1 : 1;
                // A card that wraps from one end to the other jumps instead of sweeping across the front
                var wrapped = card._o !== undefined && Math.abs(o - card._o) > n / 2;
                card._o = o;
                if (wrapped) card.classList.add("jump");

                var x = a === 0 ? 0 : s * (step + (a - 1) * step * 0.6);
                card.style.transform = "translateX(" + x.toFixed(1) + "px) translateZ(" + (-a * 230) + "px) rotateY(" +
                    (-s * Math.min(a, 1) * 42) + "deg)";
                card.style.opacity = a > 2 ? "0" : (a === 2 ? "0.5" : "1");
                card.style.filter = a === 0 ? "none" : "brightness(" + (a === 1 ? 0.62 : 0.4) + ")";
                card.style.zIndex = String(10 - a);
                card.classList.toggle("active", a === 0);
                card.setAttribute("aria-hidden", a === 0 ? "false" : "true");
                // Links in side cards stay out of the tab order; the card itself stays clickable
                var inner = card.querySelector(".pc-inner");
                if (inner) inner.inert = a !== 0;

                if (wrapped) {
                    void card.offsetWidth;
                    requestAnimationFrame(function () { card.classList.remove("jump"); });
                }
            });
            dots.forEach(function (d, i) { d.classList.toggle("active", i === active); });
            if (countEl) countEl.textContent = pad2(active + 1) + " / " + pad2(n);
        }

        function goTo(i) {
            active = ((i % n) + n) % n;
            layout();
        }

        if (prevBtn) prevBtn.addEventListener("click", function () { goTo(active - 1); });
        if (nextBtn) nextBtn.addEventListener("click", function () { goTo(active + 1); });
        track.addEventListener("keydown", function (e) {
            if (e.key === "ArrowLeft") { e.preventDefault(); goTo(active - 1); }
            if (e.key === "ArrowRight") { e.preventDefault(); goTo(active + 1); }
        });
        window.addEventListener("resize", layout);

        // Drag / swipe to move one card at a time
        var dragX = null, dragged = false;
        track.addEventListener("pointerdown", function (e) {
            if (e.button !== 0) return;
            dragX = e.clientX;
            dragged = false;
        });
        window.addEventListener("pointermove", function (e) {
            if (dragX === null) return;
            if (Math.abs(e.clientX - dragX) > 8) {
                dragged = true;
                track.classList.add("dragging");
            }
        });
        window.addEventListener("pointerup", function (e) {
            if (dragX === null) return;
            var dx = e.clientX - dragX;
            dragX = null;
            track.classList.remove("dragging");
            if (Math.abs(dx) > 50) goTo(active + (dx < 0 ? 1 : -1));
        });
        // A side card brings itself to the front; a drag never counts as a click
        cards.forEach(function (card, i) {
            card.addEventListener("click", function (e) {
                if (dragged) { e.preventDefault(); e.stopPropagation(); dragged = false; return; }
                if (i !== active) { e.preventDefault(); goTo(i); }
            }, true);
        });
        layout();

        // Autoplay: advance every few seconds, pause while the visitor interacts
        if (!reduceMotion && n > 1) {
            var paused = false, visible = false;
            var carousel = track.parentElement;
            ["mouseenter", "focusin", "touchstart"].forEach(function (ev) {
                carousel.addEventListener(ev, function () { paused = true; }, { passive: true });
            });
            ["mouseleave", "focusout"].forEach(function (ev) {
                carousel.addEventListener(ev, function () { paused = false; });
            });
            carousel.addEventListener("touchend", function () {
                setTimeout(function () { paused = false; }, 4000);
            }, { passive: true });
            if ("IntersectionObserver" in window) {
                new IntersectionObserver(function (entries) {
                    visible = entries[0].isIntersecting;
                }, { threshold: 0.3 }).observe(carousel);
            } else {
                visible = true;
            }
            setInterval(function () {
                if (paused || !visible || document.hidden) return;
                goTo(active + 1);
            }, 3600);
        }
    }

    /* ---------- Skills: category panel ---------- */
    var skillGrid = document.getElementById("skillGrid");
    var tabsWrap = document.getElementById("skillTabs");
    var skillCards = skillGrid ? Array.prototype.slice.call(skillGrid.querySelectorAll(".skill-card")) : [];
    var showCategory = null;
    if (skillCards.length && tabsWrap) {
        var cats = [], countBy = {};
        skillCards.forEach(function (c) {
            var k = c.getAttribute("data-cat") || "Other";
            if (!countBy[k]) { countBy[k] = 0; cats.push(k); }
            countBy[k]++;
        });
        tabsWrap.innerHTML = cats.map(function (c) {
            return '<button type="button" role="tab" class="skill-tab" data-cat="' + escapeHtml(c) + '">' +
                escapeHtml(c) + ' <b>' + countBy[c] + '</b></button>';
        }).join("");
        var tabs = Array.prototype.slice.call(tabsWrap.querySelectorAll(".skill-tab"));
        var currentCat = null;

        showCategory = function (cat, highlight, animate) {
            currentCat = cat;
            tabs.forEach(function (t) {
                var on = t.getAttribute("data-cat") === cat;
                t.classList.toggle("active", on);
                t.setAttribute("aria-selected", on ? "true" : "false");
            });
            var k = 0;
            skillCards.forEach(function (c) {
                var on = c.getAttribute("data-cat") === cat;
                c.hidden = !on;
                c.classList.remove("in", "hit");
                if (on && animate && !reduceMotion) {
                    c.style.setProperty("--d", (k++ * 0.05).toFixed(2) + "s");
                    void c.offsetWidth;
                    c.classList.add("in");
                }
                if (on && highlight && c.getAttribute("data-name") === highlight) c.classList.add("hit");
            });
        };
        tabs.forEach(function (t) {
            t.addEventListener("click", function () { showCategory(t.getAttribute("data-cat"), null, true); });
        });
        showCategory(cats[0], null, false);

        // Play the flip-in once the panel scrolls into view
        if ("IntersectionObserver" in window && !reduceMotion) {
            var panelIo = new IntersectionObserver(function (entries) {
                if (entries[0].isIntersecting) { showCategory(currentCat, null, true); panelIo.disconnect(); }
            }, { threshold: 0.3 });
            panelIo.observe(skillGrid);
        }
    }

    /* ---------- Skills: 3D globe of tags ---------- */
    var globe = document.getElementById("skillGlobe");
    if (globe && skillCards.length) {
        globe.setAttribute("aria-hidden", "true");
        var total = skillCards.length;
        var golden = Math.PI * (3 - Math.sqrt(5));
        var rotX = -0.3, rotY = 0, baseVy = 0.0045, baseVx = 0.0012;
        var vx = baseVx, vy = baseVy, hoverTag = false, globeDragged = false;
        var drag = null, globeVisible = false, rafId = 0;

        var points = skillCards.map(function (card, i) {
            var el = document.createElement("span");
            el.className = "globe-tag";
            var icon = card.querySelector(".skill-icon i");
            var name = card.getAttribute("data-name") || "";
            el.innerHTML = '<i class="' + escapeHtml(icon ? icon.className : "") + '"></i>' +
                escapeHtml(name.replace(/\s*\(.*?\)\s*/g, " ").trim());
            globe.appendChild(el);
            el.addEventListener("click", function () {
                if (globeDragged) return;
                points.forEach(function (p) { p.el.classList.toggle("hit", p.el === el); });
                if (showCategory) showCategory(card.getAttribute("data-cat"), name, true);
                if (window.innerWidth < 920 && skillGrid) skillGrid.scrollIntoView({ behavior: "smooth", block: "center" });
            });
            el.addEventListener("mouseenter", function () { hoverTag = true; });
            el.addEventListener("mouseleave", function () { hoverTag = false; });
            // Spread the tags evenly over the sphere (Fibonacci lattice)
            var y = 1 - (i + 0.5) / total * 2;
            var r = Math.sqrt(1 - y * y);
            var t = golden * i;
            return { el: el, x: Math.cos(t) * r, y: y, z: Math.sin(t) * r };
        });

        var render = function () {
            // Smaller radius on narrow screens so the widest tags stay inside the viewport
            var R = globe.clientWidth * (globe.clientWidth < 420 ? 0.31 : 0.39);
            var cy = Math.cos(rotY), sy = Math.sin(rotY), cx = Math.cos(rotX), sx = Math.sin(rotX);
            points.forEach(function (p) {
                var x1 = p.x * cy - p.z * sy;
                var z1 = p.x * sy + p.z * cy;
                var y2 = p.y * cx - z1 * sx;
                var z2 = p.y * sx + z1 * cx;
                var depth = (z2 + 1) / 2;                // 0 = far side, 1 = nearest
                p.el.style.transform = "translate(-50%, -50%) translate3d(" + (x1 * R).toFixed(1) + "px, " +
                    (y2 * R).toFixed(1) + "px, 0) scale(" + (0.55 + depth * 0.6).toFixed(3) + ")";
                p.el.style.opacity = (0.15 + depth * 0.85).toFixed(2);
                p.el.style.zIndex = String(Math.round(depth * 100));
            });
        };

        var frame = function () {
            rafId = 0;
            if (!drag) {
                var slow = hoverTag ? 0.12 : 1;
                vx += (baseVx * slow - vx) * 0.04;
                vy += (baseVy * slow - vy) * 0.04;
                rotX += vx;
                rotY += vy;
            }
            render();
            if (globeVisible && !document.hidden) rafId = requestAnimationFrame(frame);
        };
        var startSpin = function () { if (!rafId && !reduceMotion) rafId = requestAnimationFrame(frame); };

        globe.addEventListener("pointerdown", function (e) {
            drag = { x: e.clientX, y: e.clientY, moved: 0 };
            globeDragged = false;
            globe.classList.add("dragging");
        });
        window.addEventListener("pointermove", function (e) {
            if (!drag) return;
            var dx = e.clientX - drag.x, dy = e.clientY - drag.y;
            drag.x = e.clientX;
            drag.y = e.clientY;
            drag.moved += Math.abs(dx) + Math.abs(dy);
            if (drag.moved > 6) globeDragged = true;
            vy = dx * 0.006;
            vx = -dy * 0.006;
            rotY += vy;
            rotX += vx;
            if (reduceMotion) render();
        });
        window.addEventListener("pointerup", function () {
            if (!drag) return;
            drag = null;
            globe.classList.remove("dragging");
            // Let the click handler see whether this was a drag, then reset
            setTimeout(function () { globeDragged = false; }, 0);
        });

        render();
        if ("IntersectionObserver" in window) {
            new IntersectionObserver(function (entries) {
                globeVisible = entries[0].isIntersecting;
                if (globeVisible) startSpin();
            }).observe(globe);
        } else {
            globeVisible = true;
            startSpin();
        }
        document.addEventListener("visibilitychange", function () { if (!document.hidden) startSpin(); });
    }

    /* ---------- Hero: depth parallax with the cursor ---------- */
    var hero = document.getElementById("home");
    if (hero && !reduceMotion && window.matchMedia("(hover: hover)").matches) {
        hero.addEventListener("mousemove", function (e) {
            hero.style.setProperty("--mx", (e.clientX / window.innerWidth - 0.5).toFixed(3));
            hero.style.setProperty("--my", (e.clientY / window.innerHeight - 0.5).toFixed(3));
        });
        hero.addEventListener("mouseleave", function () {
            hero.style.setProperty("--mx", "0");
            hero.style.setProperty("--my", "0");
        });
    }

    /* ---------- Profile card: tilt toward the cursor ---------- */
    var profileVisual = document.getElementById("profileVisual");
    var profileCard = document.getElementById("profileCard");
    if (profileVisual && profileCard && !reduceMotion && window.matchMedia("(hover: hover)").matches) {
        profileVisual.addEventListener("mousemove", function (e) {
            var r = profileVisual.getBoundingClientRect();
            var x = (e.clientX - r.left) / r.width - 0.5;
            var y = (e.clientY - r.top) / r.height - 0.5;
            profileCard.style.setProperty("--ry", (x * 26).toFixed(2) + "deg");
            profileCard.style.setProperty("--rx", (-y * 18).toFixed(2) + "deg");
        });
        profileVisual.addEventListener("mouseleave", function () {
            profileCard.style.setProperty("--ry", "0deg");
            profileCard.style.setProperty("--rx", "0deg");
        });
    }

    /* ---------- 3D tilt: [data-tilt] elements lean toward the cursor ---------- */
    var canHover = window.matchMedia("(hover: hover) and (pointer: fine)").matches;
    if (!reduceMotion && canHover) {
        var tiltEl = null;
        var resetTilt = function (el) {
            el.classList.remove("tilting");
            el.style.setProperty("--rx", "0deg");
            el.style.setProperty("--ry", "0deg");
        };
        document.addEventListener("mousemove", function (e) {
            var el = e.target.closest ? e.target.closest("[data-tilt]") : null;
            if (tiltEl && tiltEl !== el) resetTilt(tiltEl);
            tiltEl = el;
            if (!el) return;
            var r = el.getBoundingClientRect();
            var x = (e.clientX - r.left) / r.width;
            var y = (e.clientY - r.top) / r.height;
            // Large surfaces tilt less so their edges don't swing too far
            var max = r.width > 520 ? 6 : 12;
            el.classList.add("tilting");
            el.style.setProperty("--rx", ((0.5 - y) * max).toFixed(2) + "deg");
            el.style.setProperty("--ry", ((x - 0.5) * max).toFixed(2) + "deg");
            el.style.setProperty("--gx", (x * 100).toFixed(1) + "%");
            el.style.setProperty("--gy", (y * 100).toFixed(1) + "%");
        }, { passive: true });
        document.addEventListener("mouseleave", function () {
            if (tiltEl) resetTilt(tiltEl);
            tiltEl = null;
        });
    }

    /* ---------- Local time in New Delhi (IST) ---------- */
    var clocks = document.querySelectorAll(".ist-clock");
    if (clocks.length) {
        var tick = function () {
            var now = new Date();
            var opts = { timeZone: "Asia/Kolkata", hour: "2-digit", minute: "2-digit", hour12: true };
            var short, long;
            try {
                short = now.toLocaleTimeString("en-US", opts);
                long = now.toLocaleTimeString("en-US", Object.assign({ second: "2-digit" }, opts));
            } catch (e) {
                return;
            }
            clocks.forEach(function (el) {
                el.textContent = (el.getAttribute("data-format") === "short" ? short : long) + " IST";
            });
        };
        tick();
        setInterval(tick, 1000);
    }

    /* ---------- Copy buttons (email / phone) ---------- */
    document.querySelectorAll("[data-copy]").forEach(function (btn) {
        btn.addEventListener("click", function () {
            var text = btn.getAttribute("data-copy");
            var done = function () {
                var icon = btn.querySelector("i");
                btn.classList.add("copied");
                if (icon) icon.className = "fa-solid fa-check";
                setTimeout(function () {
                    btn.classList.remove("copied");
                    if (icon) icon.className = "fa-regular fa-copy";
                }, 1600);
            };
            if (navigator.clipboard && window.isSecureContext) {
                navigator.clipboard.writeText(text).then(done, function () { /* ignore */ });
            } else {
                var ta = document.createElement("textarea");
                ta.value = text;
                ta.style.position = "fixed";
                ta.style.opacity = "0";
                document.body.appendChild(ta);
                ta.select();
                try { document.execCommand("copy"); done(); } catch (e) { /* ignore */ }
                ta.remove();
            }
        });
    });

    /* ---------- Contact: live payload preview + progress ---------- */
    var form = document.getElementById("contactForm");
    var payload = document.getElementById("payload");
    if (form && payload) {
        var placeholders = { name: "[Awaiting Name]", email: "[Awaiting Email]", subject: "[Awaiting Subject]", message: "[Awaiting Message]" };
        var steps = form.querySelectorAll(".form-steps i");
        var filled = function (field) {
            var input = form.querySelector('[name="' + field + '"]');
            return !!(input && input.value.trim());
        };
        var sync = function () {
            Object.keys(placeholders).forEach(function (field) {
                var input = form.querySelector('[name="' + field + '"]');
                var out = payload.querySelector('[data-field="' + field + '"]');
                if (!input || !out) return;
                var v = input.value.trim().replace(/\s+/g, " ");
                if (v.length > 60) v = v.slice(0, 60) + "…";
                out.textContent = JSON.stringify(v || placeholders[field]);
                out.classList.toggle("empty-val", !v);
            });
            // Three steps: who you are, what it's about, the message itself
            var done = [filled("name") && filled("email"), filled("subject"), filled("message")];
            steps.forEach(function (s, i) { s.classList.toggle("on", !!done[i]); });
        };
        form.addEventListener("input", sync);
        form.addEventListener("submit", function () {
            var btn = form.querySelector(".btn-send");
            if (btn) btn.classList.add("sending");
        });
        sync();
    }

    /* ---------- Footer: giant name leans with the cursor ---------- */
    var stage = document.getElementById("footerStage");
    var giant = document.getElementById("footerGiant");
    if (stage && giant && !reduceMotion && canHover) {
        var footer = stage.closest("footer") || stage;
        footer.addEventListener("mousemove", function (e) {
            var r = stage.getBoundingClientRect();
            var x = (e.clientX - r.left) / r.width - 0.5;
            var y = (e.clientY - r.top) / r.height - 0.5;
            giant.style.setProperty("--ry", (x * 18).toFixed(2) + "deg");
            giant.style.setProperty("--rx", (-y * 10).toFixed(2) + "deg");
        });
        footer.addEventListener("mouseleave", function () {
            giant.style.setProperty("--ry", "0deg");
            giant.style.setProperty("--rx", "0deg");
        });
    }

    var toTop = document.getElementById("toTop");
    if (toTop) {
        toTop.addEventListener("click", function (e) {
            e.preventDefault();
            window.scrollTo({ top: 0, behavior: reduceMotion ? "auto" : "smooth" });
        });
    }

    /* ---------- Live GitHub ---------- */
    var gh = document.getElementById("github");
    var user = gh && gh.getAttribute("data-user");
    if (!user) return;

    var LANG_COLORS = {
        Java: "#b07219", JavaScript: "#f1e05a", Python: "#3572A5", HTML: "#e34c26", CSS: "#563d7c",
        TypeScript: "#3178c6", Shell: "#89e051", Dockerfile: "#384d54", Kotlin: "#A97BFF", "C++": "#f34b7d",
        C: "#555555", Go: "#00ADD8", PHP: "#4F5D95", Jupyter: "#DA5B0B"
    };

    function escapeHtml(s) {
        return String(s == null ? "" : s).replace(/[&<>"']/g, function (c) {
            return { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c];
        });
    }

    function timeAgo(iso) {
        var days = Math.floor((Date.now() - new Date(iso).getTime()) / 86400000);
        if (days < 1) return "today";
        if (days < 30) return days + (days === 1 ? " day ago" : " days ago");
        var months = Math.floor(days / 30);
        if (months < 12) return months + (months === 1 ? " month ago" : " months ago");
        var years = Math.floor(months / 12);
        return years + (years === 1 ? " year ago" : " years ago");
    }

    function prettyName(name) {
        return name.replace(/[-_]+/g, " ").replace(/\b\w/g, function (c) { return c.toUpperCase(); });
    }

    function setStat(key, value) {
        document.querySelectorAll('[data-gh-stat="' + key + '"]').forEach(function (el) {
            if (typeof value !== "number" || reduceMotion) { el.textContent = value; return; }
            // Count up from zero
            var start = performance.now();
            (function step(now) {
                var t = Math.min((now - start) / 1200, 1);
                el.textContent = Math.round((1 - Math.pow(1 - t, 3)) * value);
                if (t < 1) requestAnimationFrame(step);
            })(start);
        });
    }

    function cached(url) {
        var key = "gh:" + url;
        try {
            var hit = JSON.parse(sessionStorage.getItem(key) || "null");
            if (hit && Date.now() - hit.t < 10 * 60 * 1000) return Promise.resolve(hit.d);
        } catch (e) { /* ignore */ }
        return fetch(url, { headers: { Accept: "application/vnd.github+json" } })
            .then(function (r) {
                if (!r.ok) throw new Error("GitHub API " + r.status);
                return r.json();
            })
            .then(function (d) {
                try { sessionStorage.setItem(key, JSON.stringify({ t: Date.now(), d: d })); } catch (e) { /* ignore */ }
                return d;
            });
    }

    var api = "https://api.github.com/users/" + encodeURIComponent(user);
    var grid = document.getElementById("repoGrid");

    // Descriptions from the portfolio's own projects, used when a repo has none (or only a stub) on GitHub
    var notes = {};
    document.querySelectorAll("#repoNotes li").forEach(function (li) {
        notes[li.getAttribute("data-url").replace(/\/+$/, "")] = li.textContent.trim();
    });

    // Cards fly in once the grid scrolls into view
    if (grid) {
        if (reduceMotion || !("IntersectionObserver" in window)) {
            grid.classList.add("play");
        } else {
            var gridIo = new IntersectionObserver(function (entries) {
                if (entries[0].isIntersecting) { grid.classList.add("play"); gridIo.disconnect(); }
            }, { threshold: 0.1 });
            gridIo.observe(grid);
        }
    }

    Promise.all([cached(api), cached(api + "/repos?per_page=100&sort=pushed")])
        .then(function (res) {
            var profile = res[0];
            var repos = res[1].filter(function (r) { return !r.fork && !r.archived; });
            var shown = repos.filter(function (r) { return r.language; });

            setStat("repos", profile.public_repos);
            if (repos.length) setStat("updated", timeAgo(repos[0].pushed_at));

            var recent = document.getElementById("ghRecent");
            if (recent) {
                recent.innerHTML = repos.slice(0, 3).map(function (r) {
                    return '<li><a href="' + escapeHtml(r.html_url) + '" target="_blank" rel="noopener" style="--c:' +
                        (LANG_COLORS[r.language] || "#8b93a7") + '"><span class="lang-dot"></span>' +
                        escapeHtml(prettyName(r.name)) + '<time datetime="' + escapeHtml(r.pushed_at) + '">' +
                        timeAgo(r.pushed_at) + '</time></a></li>';
                }).join("");
            }

            var counts = {};
            shown.forEach(function (r) { counts[r.language] = (counts[r.language] || 0) + 1; });
            var langs = Object.keys(counts).sort(function (a, b) { return counts[b] - counts[a]; });
            setStat("languages", langs.length);
            var colorOf = function (l) { return LANG_COLORS[l] || "#8b93a7"; };

            // 3D ring: one conic gradient, stacked in layers to give it thickness
            var donut = document.getElementById("langDonut");
            if (donut && shown.length) {
                var at = 0, stops = [];
                langs.forEach(function (l) {
                    var end = at + counts[l] / shown.length * 100;
                    stops.push(colorOf(l) + " " + at.toFixed(2) + "% " + (end - 0.8).toFixed(2) + "%");
                    stops.push("transparent " + (end - 0.8).toFixed(2) + "% " + end.toFixed(2) + "%");
                    at = end;
                });
                donut.style.setProperty("--donut", "conic-gradient(" + stops.join(", ") + ")");
                var layers = "";
                for (var k = 0; k < 9; k++) {
                    layers += '<span style="transform:translateZ(' + (k * 2) + 'px);filter:brightness(' +
                        (k === 8 ? 1 : (0.35 + k * 0.05)).toFixed(2) + ')"></span>';
                }
                donut.innerHTML = layers;
            }

            var legend = document.getElementById("langLegend");
            if (legend && shown.length) {
                legend.innerHTML = langs.map(function (l, n) {
                    var pct = Math.round(counts[l] / shown.length * 100);
                    return '<li style="--lc:' + colorOf(l) + '"><i></i>' + escapeHtml(l) + ' <b>' + pct + '%</b>' +
                        '<span class="bar"><span style="--w:' + pct + '%;animation-delay:' + (n * 0.12) + 's"></span></span></li>';
                }).join("");
            }

            if (!grid) return;
            if (!shown.length) {
                grid.innerHTML = '<p class="empty">No public repositories yet.</p>';
                return;
            }
            // Widen cards so the three-column grid has no gaps: 3n+2 -> first wide, 3n+1 -> first and last wide
            var n = shown.length;
            var wide = function (i) {
                return (n % 3 === 2 && i === 0) || (n % 3 === 1 && n > 1 && (i === 0 || i === n - 1));
            };
            grid.innerHTML = shown.map(function (r, i) {
                var color = colorOf(r.language);
                var desc = r.description || "";
                var note = notes[r.html_url.toLowerCase()];
                if (note && desc.length < 40) desc = note;
                return '<a class="repo-card' + (wide(i) ? " wide" : "") + '" href="' + escapeHtml(r.html_url) +
                    '" target="_blank" rel="noopener" style="--c:' + color + ';--d:' + (i * 0.09).toFixed(2) + 's">' +
                    '<div class="repo-inner" data-tilt>' +
                    '<div class="repo-top"><span class="repo-icon"><i class="fa-regular fa-folder-open"></i></span>' +
                    '<span class="repo-lang"><span class="lang-dot"></span>' + escapeHtml(r.language) + '</span>' +
                    '<span class="repo-go"><i class="fa-solid fa-arrow-up"></i></span></div>' +
                    '<h3>' + escapeHtml(prettyName(r.name)) + '</h3>' +
                    '<p>' + escapeHtml(desc || "Source code, README and setup notes on GitHub.") + '</p>' +
                    '<div class="repo-foot">' +
                    '<span><i class="fa-regular fa-star"></i>' + r.stargazers_count + '</span>' +
                    '<span><i class="fa-solid fa-code-fork"></i>' + r.forks_count + '</span>' +
                    '<span><i class="fa-regular fa-clock"></i>' + timeAgo(r.pushed_at) + '</span>' +
                    '</div></div></a>';
            }).join("");
        })
        .catch(function () {
            if (grid) {
                grid.classList.add("play");
                grid.innerHTML = '<p class="empty">Couldn\'t load GitHub right now — ' +
                    '<a class="link" href="https://github.com/' + encodeURIComponent(user) +
                    '" target="_blank" rel="noopener">view the profile on GitHub</a>.</p>';
            }
        });
})();
