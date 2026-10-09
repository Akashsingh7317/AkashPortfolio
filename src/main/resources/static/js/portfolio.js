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

    /* ---------- Projects carousel ---------- */
    var track = document.getElementById("carouselTrack");
    if (track) {
        var cards = Array.prototype.slice.call(track.querySelectorAll(".project-card"));
        var dotsWrap = document.getElementById("carouselDots");
        var prevBtn = document.getElementById("carouselPrev");
        var nextBtn = document.getElementById("carouselNext");
        var active = -1;

        var dots = cards.map(function (card, i) {
            var b = document.createElement("button");
            b.type = "button";
            b.setAttribute("aria-label", "Show project " + (i + 1));
            b.addEventListener("click", function () { goTo(i); });
            dotsWrap && dotsWrap.appendChild(b);
            return b;
        });

        function goTo(i) {
            var card = cards[Math.max(0, Math.min(i, cards.length - 1))];
            track.scrollTo({ left: card.offsetLeft - (track.clientWidth - card.offsetWidth) / 2, behavior: "smooth" });
        }

        function updateActive() {
            var center = track.scrollLeft + track.clientWidth / 2;
            var best = 0, bestDist = Infinity;
            cards.forEach(function (card, i) {
                var d = Math.abs(card.offsetLeft + card.offsetWidth / 2 - center);
                if (d < bestDist) { bestDist = d; best = i; }
            });
            if (best === active) return;
            active = best;
            cards.forEach(function (c, i) { c.classList.toggle("active", i === best); });
            dots.forEach(function (d, i) { d.classList.toggle("active", i === best); });
            if (prevBtn) prevBtn.disabled = best === 0;
            if (nextBtn) nextBtn.disabled = best === cards.length - 1;
        }

        if (prevBtn) prevBtn.addEventListener("click", function () { goTo(active - 1); });
        if (nextBtn) nextBtn.addEventListener("click", function () { goTo(active + 1); });
        track.addEventListener("scroll", function () { requestAnimationFrame(updateActive); }, { passive: true });
        window.addEventListener("resize", updateActive);
        // Clicking a dimmed side card brings it to the centre instead of following its link
        cards.forEach(function (card, i) {
            card.addEventListener("click", function (e) {
                if (i !== active && !e.target.closest("a[target]")) { e.preventDefault(); goTo(i); }
            });
        });
        updateActive();

        // Autoplay: advance every few seconds, loop to the start, pause while the visitor interacts
        if (!reduceMotion && cards.length > 1) {
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
                goTo(active >= cards.length - 1 ? 0 : active + 1);
            }, 3200);
        }
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

    /* ---------- Contact: live payload preview ---------- */
    var form = document.getElementById("contactForm");
    var payload = document.getElementById("payload");
    if (form && payload) {
        var placeholders = { name: "[Awaiting Name]", email: "[Awaiting Email]", subject: "[Awaiting Subject]", message: "[Awaiting Message]" };
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
        };
        form.addEventListener("input", sync);
        sync();
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
        document.querySelectorAll('[data-gh-stat="' + key + '"]').forEach(function (el) { el.textContent = value; });
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

    Promise.all([cached(api), cached(api + "/repos?per_page=100&sort=pushed")])
        .then(function (res) {
            var profile = res[0];
            var repos = res[1].filter(function (r) { return !r.fork && !r.archived; });
            var shown = repos.filter(function (r) { return r.language; });

            setStat("repos", profile.public_repos);
            if (repos.length) setStat("updated", timeAgo(repos[0].pushed_at));

            var counts = {};
            shown.forEach(function (r) { counts[r.language] = (counts[r.language] || 0) + 1; });
            var langs = Object.keys(counts).sort(function (a, b) { return counts[b] - counts[a]; });
            setStat("languages", langs.length);

            var bar = document.getElementById("langBar");
            var legend = document.getElementById("langLegend");
            if (bar && legend && shown.length) {
                bar.innerHTML = langs.map(function (l) {
                    return '<span title="' + escapeHtml(l) + '" style="width:' + (counts[l] / shown.length * 100) +
                        '%;background:' + (LANG_COLORS[l] || "#8b93a7") + '"></span>';
                }).join("");
                legend.innerHTML = langs.map(function (l) {
                    return '<li><i style="background:' + (LANG_COLORS[l] || "#8b93a7") + '"></i>' + escapeHtml(l) +
                        ' <span>' + Math.round(counts[l] / shown.length * 100) + '%</span></li>';
                }).join("");
            }

            if (!grid) return;
            if (!shown.length) {
                grid.innerHTML = '<p class="empty">No public repositories yet.</p>';
                return;
            }
            // Avoid a lone card on the last row of a three-column grid
            grid.classList.toggle("two-col", shown.length % 3 === 1 && shown.length <= 4);
            grid.innerHTML = shown.map(function (r) {
                var color = LANG_COLORS[r.language] || "#8b93a7";
                return '<a class="repo-card" href="' + escapeHtml(r.html_url) + '" target="_blank" rel="noopener">' +
                    '<h3><i class="fa-regular fa-folder"></i>' + escapeHtml(prettyName(r.name)) +
                    '<i class="fa-solid fa-arrow-up-right-from-square ext"></i></h3>' +
                    '<p>' + escapeHtml(r.description || "No description provided.") + '</p>' +
                    '<div class="repo-foot">' +
                    '<span><span class="lang-dot" style="background:' + color + '"></span>' + escapeHtml(r.language) + '</span>' +
                    '<span><i class="fa-regular fa-star"></i>' + r.stargazers_count + '</span>' +
                    '<span><i class="fa-regular fa-clock"></i>' + timeAgo(r.pushed_at) + '</span>' +
                    '</div></a>';
            }).join("");
        })
        .catch(function () {
            if (grid) {
                grid.innerHTML = '<p class="empty">Couldn\'t load GitHub right now — ' +
                    '<a class="link" href="https://github.com/' + encodeURIComponent(user) +
                    '" target="_blank" rel="noopener">view the profile on GitHub</a>.</p>';
            }
        });
})();
