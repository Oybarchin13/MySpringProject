document.addEventListener('DOMContentLoaded', function () {
    // ─── Profile Dropdown ───────────────────────────────────────────
    const profileBtn  = document.getElementById('profileBtn');
    const dropdownMenu = document.getElementById('dropdownMenu');

    if (profileBtn && dropdownMenu) {

        // Tugma bosilganda ochish / yopish
        profileBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            dropdownMenu.classList.toggle('hidden');
        });

        // Tashqariga bosisa yopish
        document.addEventListener('click', function (e) {
            if (!profileBtn.contains(e.target)) {
                dropdownMenu.classList.add('hidden');
            }
        });

        // ESC tugmasi bilan yopish
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') {
                dropdownMenu.classList.add('hidden');
            }
        });
    }
});