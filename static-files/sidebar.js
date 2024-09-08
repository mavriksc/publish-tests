const xhttp = new XMLHttpRequest()
xhttp.onload = function () {
  const appListElement = document.getElementById("appList")
  const appsAndBranches = JSON.parse(this.responseText)

  appListElement.innerHTML = Object.keys(appsAndBranches).map(app => {
    const branchHTML = appsAndBranches[app].map(branch => `<div class="branch ${app} ${branch}" onclick="showReport('${app}','${branch}')">${branch}</div>`).join('')
    return `<div class="appName" id="${app}"><button class="btn" id="${app}-btn" onclick="showToggle('${app}')">▶ ${app}</button>
                        <div class="collapse" id="${app}-collapse">
                            ${branchHTML}
                        </div></div>`

  }).join('')
}
xhttp.open("GET", "apps.json", true)
xhttp.send()

function showReport(appName, branch) {
  let previous = document.getElementsByClassName('selected')
  if (previous.length>0) previous[0].classList.toggle('selected')
  document.getElementsByClassName(`branch ${appName} ${branch}`)[0].classList.toggle('selected')
  console.log(`${appName}/${branch}`)
  document.getElementById('showReport').src = `reports/${appName}/${branch}/test/index.html`;
}

function showToggle(appName) {
  let div = document.getElementById(`${appName}-collapse`)
  div.classList.toggle("collapse")

  let button = document.getElementById(`${appName}-btn`)
  if (button.innerText.includes('▶'))
    button.innerText = button.innerText.replace('▶', '▼')
  else
    button.innerText = button.innerText.replace('▼', '▶')
}

function filterBranches(e) {
  let branches = document.getElementsByClassName('branch')
  for (let i = 0; i < branches.length; i++) {
    let branchContainsSearchText = branches[i].innerText.includes(e.value)
    if ((e.value.length < 1 || branchContainsSearchText) && branches[i].classList.contains("filtered")) {
      branches[i].classList.remove("filtered")
    } else if (!branchContainsSearchText) {
      branches[i].classList.add("filtered")
    }
  }

  let apps = document.getElementsByClassName('appName')
  for (let i = 0; i < apps.length; i++) {
    let app = apps[i].id
    let filtered = document.getElementsByClassName(`filtered ${app}`)
    let filteredCount = filtered != null ? filtered.length : 0
    let branchCount = document.getElementsByClassName(`${app}`).length
    let allBranchesFiltered = filteredCount === branchCount
    if (allBranchesFiltered && !apps[i].classList.contains("filtered"))
      apps[i].classList.add("filtered")
    else if (!allBranchesFiltered)
      apps[i].classList.remove("filtered")
  }

}
