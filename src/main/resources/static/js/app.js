// 分页配置
const PAGE_SIZE = 5;
let currentPage = 1;
let totalPages = 1;
let allRecords = [];  // 存储所有记录

// 当前操作的记录ID
let currentRecordId = null;
let currentAction = null;

// 初始化页面
document.addEventListener('DOMContentLoaded', function() {
    loadRecords();

    // 模态框按钮事件
    document.getElementById('modal-cancel').addEventListener('click', hideModal);
    document.getElementById('modal-approve').addEventListener('click', submitReview);
    document.getElementById('modal-reject').addEventListener('click', submitReview);

    // 搜索筛选功能
    const nameFilter = document.getElementById('name-filter');
    const typeFilter = document.getElementById('type-filter');
    const statusFilter = document.getElementById('status-filter');

    // 监听搜索条件变化
    nameFilter.addEventListener('input', function() {
        currentPage = 1;
        loadRecords();
    });

    typeFilter.addEventListener('change', function() {
        currentPage = 1;
        loadRecords();
    });

    statusFilter.addEventListener('change', function() {
        currentPage = 1;
        loadRecords();
    });

    // 分页按钮事件
    document.getElementById('prev-page').addEventListener('click', function() {
        if (currentPage > 1) {
            currentPage--;
            loadRecords();
        }
    });

    document.getElementById('next-page').addEventListener('click', function() {
        if (currentPage < totalPages) {
            currentPage++;
            loadRecords();
        }
    });
});

// 从API加载记录
function loadRecords() {
    const name = document.getElementById('name-filter').value.toLowerCase().trim();
    const type = document.getElementById('type-filter').value;
    const status = document.getElementById('status-filter').value;

    // 构建查询参数
    let queryParams = `?page=${currentPage}&pageSize=${PAGE_SIZE}`;
    if (name) queryParams += `&name=${encodeURIComponent(name)}`;
    if (type) queryParams += `&type=${type}`;
    if (status) queryParams += `&status=${status}`;

    // 显示加载状态
    document.getElementById('review-table-body').innerHTML = `
        <tr>
            <td colspan="8" class="text-center">
                <i class="fa fa-spinner"></i> 加载中...
            </td>
        </tr>
    `;

    // 发送API请求
    fetch(`/api/records${queryParams}`)
        .then(response => response.json())
        .then(data => {
            allRecords = data.records || [];
            renderRecords(allRecords);
            updatePagination(data.total || 0);
        })
        .catch(error => {
            console.error('Error loading records:', error);
            document.getElementById('review-table-body').innerHTML = `
                <tr>
                    <td colspan="8" class="text-center">
                        <i class="fa fa-exclamation-circle"></i> 加载数据失败，请重试
                    </td>
                </tr>
            `;
        });
}

// 渲染记录到表格
function renderRecords(records) {
    const tableBody = document.getElementById('review-table-body');
    tableBody.innerHTML = '';

    if (records.length === 0) {
        document.getElementById('no-result').classList.remove('hidden');
        return;
    }

    document.getElementById('no-result').classList.add('hidden');

    records.forEach(record => {
        const row = document.createElement('tr');
        row.setAttribute('data-id', record.record_id);
        row.setAttribute('data-name', record.username);
        row.setAttribute('data-type', record.project_type);
        row.setAttribute('data-status', record.status);

        // 设置项目类型徽章类
        const typeBadgeClass = {
            'energy': 'badge-energy',
            'transport': 'badge-transport',
            'recycle': 'badge-recycle',
            'water': 'badge-water',
            'other': 'badge-other'
        }[record.project_type] || 'badge-other';

        // 设置状态徽章类
        const statusBadgeClass = {
            'pending': 'status-pending',
            'approved': 'status-approved',
            'rejected': 'status-rejected'
        }[record.status] || 'status-pending';

        // 构建行内容
        row.innerHTML = `
            <td>${record.record_id}</td>
            <td>${record.user_id}</td>
            <td>${record.username}</td>
            <td><span class="badge ${typeBadgeClass}">${getTypeText(record.project_type)}</span></td>
            <td class="truncate">${record.description}</td>
            <td>${record.score}</td>
            <td><span class="status-badge ${statusBadgeClass}">${getStatusText(record.status)}</span></td>
            <td class="text-right">
                ${record.status === 'pending' ? `
                    <button class="text-green-600 hover:text-green-900 mr-3" onclick="handleApprove('${record.record_id}')">
                        <i class="fa fa-check"></i> 通过
                    </button>
                    <button class="text-red-600 hover:text-red-900" onclick="handleReject('${record.record_id}')">
                        <i class="fa fa-times"></i> 拒绝
                    </button>
                ` : `
                    <button class="text-gray-400 cursor-not-allowed" disabled>
                        <i class="fa fa-check"></i> 通过
                    </button>
                    <button class="text-gray-400 cursor-not-allowed" disabled>
                        <i class="fa fa-times"></i> 拒绝
                    </button>
                `}
            </td>
        `;

        tableBody.appendChild(row);
    });
}

// 更新分页控件
function updatePagination(totalRecords) {
    totalPages = Math.ceil(totalRecords / PAGE_SIZE);

    const startCount = document.getElementById('start-count');
    const endCount = document.getElementById('end-count');
    const totalCount = document.getElementById('total-count');

    const start = (currentPage - 1) * PAGE_SIZE + 1;
    const end = Math.min(currentPage * PAGE_SIZE, totalRecords);

    startCount.textContent = start;
    endCount.textContent = end;
    totalCount.textContent = totalRecords;

    // 更新分页按钮状态
    document.getElementById('prev-page').disabled = currentPage === 1;
    document.getElementById('next-page').disabled = currentPage === totalPages || totalPages === 0;

    // 生成页码
    const pageNumbers = document.getElementById('page-numbers');
    pageNumbers.innerHTML = '';

    // 只显示当前页附近的页码
    let startPage = Math.max(1, currentPage - 2);
    let endPage = Math.min(totalPages, startPage + 4);

    if (endPage - startPage < 4 && startPage > 1) {
        startPage = Math.max(1, endPage - 4);
    }

    // 添加第一页按钮
    if (startPage > 1) {
        addPageButton(1);
        if (startPage > 2) {
            addEllipsis();
        }
    }

    // 添加中间页码
    for (let i = startPage; i <= endPage; i++) {
        addPageButton(i);
    }

    // 添加最后一页按钮
    if (endPage < totalPages) {
        if (endPage < totalPages - 1) {
            addEllipsis();
        }
        addPageButton(totalPages);
    }
}

// 添加页码按钮
function addPageButton(pageNum) {
    const button = document.createElement('button');
    button.className = `page-btn ${pageNum === currentPage ? 'active' : ''}`;
    button.textContent = pageNum;

    button.addEventListener('click', function() {
        if (pageNum !== currentPage) {
            currentPage = pageNum;
            loadRecords();
        }
    });

    document.getElementById('page-numbers').appendChild(button);
}

// 添加省略号
function addEllipsis() {
    const ellipsis = document.createElement('span');
    ellipsis.className = 'px-2 text-gray-500';
    ellipsis.textContent = '...';
    document.getElementById('page-numbers').appendChild(ellipsis);
}

// 辅助函数：获取类型文本
function getTypeText(type) {
    const types = {
        'energy': '节能降耗',
        'transport': '绿色出行',
        'recycle': '减废回收',
        'water': '节约用水',
        'other': '其他类型'
    };
    return types[type] || type;
}

// 辅助函数：获取状态文本
function getStatusText(status) {
    const statuses = {
        'pending': '待审核',
        'approved': '已通过',
        'rejected': '已拒绝'
    };
    return statuses[status] || status;
}

// 处理审核操作
function handleApprove(recordId) {
    currentRecordId = recordId;
    currentAction = 'approve';
    showModal(recordId);
}

function handleReject(recordId) {
    currentRecordId = recordId;
    currentAction = 'reject';
    showModal(recordId);
}

// 显示审核模态框
function showModal(recordId) {
    const row = document.querySelector(`tr[data-id="${recordId}"]`);
    if (!row) return;

    // 填充模态框数据
    document.getElementById('modal-record-id').textContent = row.querySelector('td:first-child').textContent;
    document.getElementById('modal-user').textContent = row.querySelector('td:nth-child(3)').textContent;
    document.getElementById('modal-score').textContent = row.querySelector('td:nth-child(6)').textContent;
    document.getElementById('modal-description').textContent = row.querySelector('td:nth-child(5)').textContent;
    document.getElementById('review-comment').value = '';

    // 显示模态框
    const modal = document.getElementById('review-modal');
    modal.classList.remove('hidden');
    setTimeout(() => {
        modal.classList.add('show');
    }, 10);
}

// 隐藏审核模态框
function hideModal() {
    const modal = document.getElementById('review-modal');
    modal.classList.remove('show');
    setTimeout(() => {
        modal.classList.add('hidden');
    }, 200);
    currentRecordId = null;
    currentAction = null;
}

// 提交审核结果
function submitReview() {
    if (!currentRecordId || !currentAction) return;

    const comment = document.getElementById('review-comment').value;
    const newStatus = currentAction === 'approve' ? 'approved' : 'rejected';

    // 更新数据库
    fetch(`/api/records/${currentRecordId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            status: newStatus,
            comment: comment
        })
    })
        .then(response => response.json())
        .then(data => {
            // 更新UI
            const row = document.querySelector(`tr[data-id="${currentRecordId}"]`);
            if (row) {
                // 更新状态
                if (currentAction === 'approve') {
                    row.querySelector('td:nth-child(7) span').className = 'status-badge status-approved';
                    row.querySelector('td:nth-child(7) span').textContent = '已通过';
                } else {
                    row.querySelector('td:nth-child(7) span').className = 'status-badge status-rejected';
                    row.querySelector('td:nth-child(7) span').textContent = '已拒绝';
                }

                // 禁用操作按钮
                const buttons = row.querySelectorAll('td:nth-child(8) button');
                buttons.forEach(button => {
                    button.className = 'text-gray-400 cursor-not-allowed';
                    button.disabled = true;
                });

                // 更新数据属性
                row.setAttribute('data-status', newStatus);
            }

            // 隐藏模态框
            hideModal();

            // 显示成功提示
            alert(`${currentAction === 'approve' ? '通过' : '拒绝'}审核成功`);
        })
        .catch(error => {
            console.error('Error updating record:', error);
            alert('操作失败，请重试');
        });
}