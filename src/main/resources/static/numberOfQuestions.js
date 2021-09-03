const select = document.querySelector('#number_of_questions')
select.onchange = (elem) => {
    const n = 4
    const lastDisplayable = elem.currentTarget.selectedIndex + 1

    const display = new Array(n)
    for (let i = 0; i < n; i++) {
        if (i <= lastDisplayable) {
            display[i] = ''
        } else {
            display[i] = 'none'
        }
    }

    const answers = []
    for (let i = 0; i < n; i++) {
        const answer = document.querySelector('#answer_' + (i + 1))
        answers.push(answer)
    }

    for (let i = 0; i < n; i++) {
        answers[i].style.display = display[i]
    }
}
