function deleteCommunity(id) {
    if (confirm('정말 삭제하시겠습니까?')) {
        fetch(`/community/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                alert('삭제 실패');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}
