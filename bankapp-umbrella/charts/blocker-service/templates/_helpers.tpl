cl{{- define "blocker-service.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "blocker-service.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "blocker-service.selectorLabels" -}}
app: {{ include "blocker-service.name" . }}
{{- end }}

{{- define "blocker-service.labels" -}}
app: {{ include "blocker-service.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}