/**
 * Student Profile Portal - Client-side dynamic interactions
 */
document.addEventListener('DOMContentLoaded', () => {
    // Subtle entrance animation for student cards
    const cards = document.querySelectorAll('.student-card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(15px)';
        setTimeout(() => {
            card.style.transition = 'all 0.4s cubic-bezier(0.16, 1, 0.3, 1)';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 75);
    });

    // Confirmation for logout
    const logoutBtn = document.getElementById('btn-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            if (!confirm('Are you sure you want to end your current session and logout?')) {
                e.preventDefault();
            }
        });
    }
});
